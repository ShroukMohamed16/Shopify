package com.example.shopify.Payment.UI.View

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.shopify.Payment.Model.DataClass.Order
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.example.shopify.AllOrdersFragment.Model.*
import com.example.shopify.CartFragment.UI.View.formatDecimal
import com.example.shopify.CartFragment.UI.viewmodel.CartViewModel
import com.example.shopify.CartFragment.UI.viewmodel.CartViewModelFactory
import com.example.shopify.CartFragment.model.CartRepository
import com.example.shopify.CartFragment.remote.CartClient
import com.example.shopify.Payment.Model.DataClass.DiscountCode
import com.example.shopify.Payment.Model.DataClass.LineItemm
import com.example.shopify.Payment.Model.DataClass.OrderData
import com.example.shopify.Payment.Model.Repository.PaymentRepository
import com.example.shopify.Payment.Remote.PaymentClient
import com.example.shopify.Payment.UI.ViewModel.PaymentViewModel
import com.example.shopify.Payment.UI.ViewModel.PaymentViewModelFactory
import com.example.shopify.R
import com.example.shopify.base.State
import com.example.shopify.base.line_items
import com.example.shopify.databinding.DeleteCartItemDialogBinding
import com.example.shopify.databinding.DoneAlertBinding
import com.example.shopify.databinding.FragmentPaymentBinding
import com.example.shopify.homeFragment.Model.DataCalss.DiscountCodes
import com.example.shopify.utilities.MyPriceRules
import com.example.shopify.utilities.MySharedPreferences
import com.example.shopify.utilities.checkConnectivity
import com.example.shopify.utilities.createAlert
import com.paypal.checkout.approve.OnApprove
import com.paypal.checkout.cancel.OnCancel
import com.paypal.checkout.createorder.CreateOrder
import com.paypal.checkout.createorder.CurrencyCode
import com.paypal.checkout.createorder.OrderIntent
import com.paypal.checkout.createorder.UserAction
import com.paypal.checkout.error.OnError
import com.paypal.checkout.order.Amount
import com.paypal.checkout.order.AppContext
import com.paypal.checkout.order.OrderRequest
import com.paypal.checkout.order.PurchaseUnit
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class PaymentFragment : Fragment() {
    lateinit var paymentBinding: FragmentPaymentBinding

    lateinit var cartViewModel: CartViewModel
    lateinit var cartViewModelFactory: CartViewModelFactory
    lateinit var viewModel: PaymentViewModel
    lateinit var factory: PaymentViewModelFactory
    lateinit var discountCodeET: EditText
    lateinit var discountCodeVerficattion: ImageView
    lateinit var couponList: MutableList<String>
    lateinit var productFeesTV: TextView
    lateinit var deliveryFeesTv: TextView
    lateinit var discountFeesTv: TextView
    lateinit var totalPriceTv: TextView

    private var deliveryPrice: Float = 20f

    private var discountPrice: Float = 0f
    private var productPrice: Float = 0f
    private var totalPrice: Float = 50f

    private lateinit var orderLineItemsList: List<LineItemm>

    lateinit var discountCodes: List<DiscountCode>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        paymentBinding = FragmentPaymentBinding.inflate(inflater, container, false)
        return paymentBinding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        factory = PaymentViewModelFactory(PaymentRepository.getInstance(PaymentClient()))
        viewModel = ViewModelProvider(this, factory).get(PaymentViewModel::class.java)
        cartViewModelFactory =
            CartViewModelFactory(CartRepository.getInstance(CartClient.getInstance()))
        cartViewModel = ViewModelProvider(this, cartViewModelFactory)[CartViewModel::class.java]

        cartViewModel.getCartDraftOrderById(
            MySharedPreferences.getInstance(requireContext()).getCartID().toString()
        )
        if (checkConnectivity(requireContext())) {

            lifecycleScope.launch {

                cartViewModel.getCart.collect { result ->
                    when (result) {
                        is State.Success -> {
                            var lineItemsList = result.data.draft_order!!.line_items
                            var mutableLineItems = mutableListOf<LineItemm>()
                            Log.i("TAG", "lineItemsList: $lineItemsList")
                            for (i in 1 until lineItemsList.size) {
                                mutableLineItems.add(
                                    LineItemm(
                                        lineItemsList[i].quantity!!,
                                        lineItemsList[i].variant_id!!
                                    )
                                )
                            }
                            orderLineItemsList = mutableLineItems.toList()
                        }
                        else -> {

                        }
                    }
                }

            }
        }

        productPrice = MySharedPreferences.getInstance(requireContext()).getTotalPrice()!!.toFloat()

        totalPrice = productPrice + deliveryPrice
        totalPrice -= discountPrice
        discountCodeET = paymentBinding.discountCodeET
        discountCodeVerficattion = paymentBinding.verfyIcon
        productFeesTV = paymentBinding.productFees
        deliveryFeesTv = paymentBinding.deliveryFeesET
        discountFeesTv = paymentBinding.DiscountValueET
        totalPriceTv = paymentBinding.totalPriceTV

        couponList = mutableListOf<String>()
        for (i in 0 until MyPriceRules.rules.size) {
            couponList.add(MyPriceRules.rules[i].title)
        }


        productFeesTV.text = "${getString(R.string.product_fees)} ${
            productPrice.times(
                MySharedPreferences.getInstance(requireContext()).getExchangeRate()
            )
        } ${MySharedPreferences.getInstance(requireContext()).getCurrencyCode()}"
        deliveryFeesTv.text = "${getString(R.string.delivery_fees)}${
            deliveryPrice.times(
                MySharedPreferences.getInstance(requireContext()).getExchangeRate()
            )
        } ${MySharedPreferences.getInstance(requireContext()).getCurrencyCode()}"
        discountFeesTv.text = "${getString(R.string.discount)}${
            discountPrice.times(
                MySharedPreferences.getInstance(requireContext()).getExchangeRate()
            )
        } ${MySharedPreferences.getInstance(requireContext()).getCurrencyCode()}"
        totalPriceTv.text = "${
            totalPrice.times(
                MySharedPreferences.getInstance(requireContext()).getExchangeRate()
            )
        } ${MySharedPreferences.getInstance(requireContext()).getCurrencyCode()}"

        discountCodeET.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Check if the entered discount code is valid
                val code = s.toString()
                if (couponList.contains(code)) {
                    val checkmark = resources.getDrawable(R.drawable.true_copoun)
                    checkmark.setBounds(0, 0, checkmark.intrinsicWidth, checkmark.intrinsicHeight)
                    discountCodeVerficattion.setImageDrawable(checkmark)
                    discountCodeVerficattion.visibility = View.VISIBLE
                    val animator = ObjectAnimator.ofFloat(discountCodeVerficattion, "alpha", 0f, 1f)
                    animator.duration = 1000
                    animator.start()

                    discountCodeET.isEnabled = false
                } else {
                    val cross = resources.getDrawable(R.drawable.false_copoun)
                    cross.setBounds(0, 0, cross.intrinsicWidth, cross.intrinsicHeight)
                    discountCodeVerficattion.setImageDrawable(cross)
                    discountCodeVerficattion.visibility = View.VISIBLE
                    val animator = ObjectAnimator.ofFloat(discountCodeVerficattion, "alpha", 0f, 1f)
                    animator.duration = 1000
                    animator.start()
                }
            }

            override fun afterTextChanged(s: Editable?) {
                var mustableDisount = mutableListOf<DiscountCode>()
                discountPrice = if (discountCodeET.text.toString() == "Summer60") {
                    mustableDisount.add(DiscountCode("Summer60", "60", "percentage"))
                    discountCodes = mustableDisount.toList()
                    60f


                } else if (discountCodeET.text.toString() == "Summer25") {

                    mustableDisount.add(DiscountCode("Summer60", "60", "fixed_amount"))
                    discountCodes = mustableDisount.toList()

                    (productPrice * (1 - .25f))
                } else {
                    mustableDisount.add(DiscountCode("", "0", "percentage"))
                    discountCodes = mustableDisount.toList()

                    0f
                }
                totalPrice = productPrice + deliveryPrice
                totalPrice -= discountPrice
                productFeesTV.text = "${getString(R.string.product_fees)} ${
                    formatDecimal(
                        productPrice.times(
                            MySharedPreferences.getInstance(requireContext()).getExchangeRate()
                        ).toDouble()
                    )
                } "
                deliveryFeesTv.text = "${getString(R.string.delivery_fees)}${
                    formatDecimal(
                        deliveryPrice.times(
                            MySharedPreferences.getInstance(requireContext()).getExchangeRate()
                        ).toDouble()
                    )
                }"
                discountFeesTv.text = "${getString(R.string.discount)}${
                    formatDecimal(
                        discountPrice.times(
                            MySharedPreferences.getInstance(requireContext()).getExchangeRate()
                        ).toDouble()
                    )
                }"
                totalPriceTv.text = formatDecimal(
                    totalPrice.times(
                        MySharedPreferences.getInstance(requireContext()).getExchangeRate()
                    ).toDouble()
                )

            }
        })

        paymentBinding.onlinePaymentRadioButton.setOnClickListener {
            paymentBinding.checkoutBtn.visibility = View.GONE
            paymentBinding.paymentButtonContainer.visibility = View.VISIBLE
        }
        paymentBinding.cashPaymentRadioButton.setOnClickListener {
            paymentBinding.checkoutBtn.visibility = View.VISIBLE
            paymentBinding.paymentButtonContainer.visibility = View.GONE
        }
        if (checkConnectivity(requireContext())) {
            paymentBinding.checkoutBtn.setOnClickListener {
                if (totalPrice > 500) {
                    createAlert(
                        getString(R.string.cant_pay_in_cash),
                        getString(R.string.large_amount_of_money), requireContext())

                } else {
                    val email =
                        MySharedPreferences.getInstance(requireContext()).getCustomerEmail()

                    val order = Order(email = email!!,
                        line_items = orderLineItemsList,
                        discount_codes = discountCodes)
                    val orderData = OrderData(order)
                    viewModel.postOrder(orderData)
                    createAlertGoToHome()
                }
            }
        } else {
            createAlert(getString(R.string.network_lost),
                getString(R.string.network_lost_title),
                requireContext())
        }
        if (checkConnectivity(requireContext())) {
            paymentBinding.paymentButtonContainer.setup(
                createOrder =
                CreateOrder { createOrderActions ->
                    val order =
                        OrderRequest(
                            intent = OrderIntent.CAPTURE,
                            appContext = AppContext(userAction = UserAction.PAY_NOW),
                            purchaseUnitList =
                            listOf(
                                PurchaseUnit(
                                    amount =
                                    Amount(
                                        currencyCode = CurrencyCode.USD,
                                        value = totalPrice.toString()
                                    )
                                )
                            )
                        )
                    createOrderActions.create(order)
                },
                onApprove =
                OnApprove { approval ->
                    approval.orderActions.capture { captureOrderResult ->
                        Log.i("MYTAG", "CaptureOrderResult: $captureOrderResult")
                        val email =
                            MySharedPreferences.getInstance(requireContext()).getCustomerEmail()

                        val order = Order(email = email!!,
                            line_items = orderLineItemsList,
                            discount_codes = discountCodes)
                        val orderData = OrderData(order)
                        viewModel.postOrder(orderData)
                        createAlertGoToHome()
                    }
                }, onCancel = OnCancel {
                    Log.d("MYTAG", "Buyer canceled the PayPal experience.")
                    Toast.makeText(requireContext(), "Payment cancelled", Toast.LENGTH_SHORT).show()
                }, onError = OnError { errorInfo ->
                    Log.d("MYTAG", "Error: $errorInfo")
                    Toast.makeText(
                        requireContext(),
                        "Payment failure $errorInfo",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            )

        } else {
            createAlert(getString(R.string.network_lost),
                getString(R.string.network_lost_title),
                requireContext())
        }

    }

    fun createAlertGoToHome() {
        val builder = AlertDialog.Builder(requireContext())
        val doneDialog = DoneAlertBinding.inflate(layoutInflater)
        builder.setView(doneDialog.root)
        val dialog = builder.create()
        dialog.show()
        doneDialog.goHOme.setOnClickListener {
            Navigation.findNavController(requireView())
                .navigate(R.id.action_paymentFragment_to_homeFragment)
            dialog.dismiss()
        }


    }

}