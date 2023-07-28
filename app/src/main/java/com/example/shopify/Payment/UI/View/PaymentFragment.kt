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
import androidx.lifecycle.ViewModelProvider
import com.example.shopify.AllOrdersFragment.Model.*
import com.example.shopify.Payment.Model.DataClass.DiscountCode
import com.example.shopify.Payment.Model.DataClass.LineItemm
import com.example.shopify.Payment.Model.DataClass.OrderData
import com.example.shopify.Payment.Model.Repository.PaymentRepository
import com.example.shopify.Payment.Remote.PaymentClient
import com.example.shopify.Payment.UI.ViewModel.PaymentViewModel
import com.example.shopify.Payment.UI.ViewModel.PaymentViewModelFactory
import com.example.shopify.R
import com.example.shopify.databinding.FragmentPaymentBinding
import com.example.shopify.utilities.MyPriceRules
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
import com.example.shopify.Payment.Model.DataClass.Order
import com.example.shopify.utilities.MySharedPreferences


class PaymentFragment : Fragment() {
    lateinit var paymentBinding: FragmentPaymentBinding
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
    private var productPrice: Float = 1500f

    private var totalPrice: Float = 50f


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

        totalPrice = productPrice - deliveryPrice
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


        productFeesTV.text = "${getString(R.string.product_fees)}$productPrice"
        deliveryFeesTv.text = "${getString(R.string.delivery_fees)}$deliveryPrice"
        discountFeesTv.text = "${getString(R.string.discount)}$discountPrice"
        totalPriceTv.text = totalPrice.toString()

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
                discountPrice = if (discountCodeET.text.toString() == "Summer60") {
                    60f
                } else if (discountCodeET.text.toString() == "Summer25") {
                    productPrice - (productPrice * .25f)
                } else {
                    0f
                }
                productFeesTV.text = "${getString(R.string.product_fees)} $productPrice"
                deliveryFeesTv.text = "${getString(R.string.delivery_fees)} $deliveryPrice"
                discountFeesTv.text = "${getString(R.string.discount)} $discountPrice"
                totalPriceTv.text = totalPrice.toString()
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
        paymentBinding.checkoutBtn.setOnClickListener {
            if (totalPrice > 1000) {
                createAlert(getString(R.string.cant_pay_in_cash),getString(R.string.large_amount_of_money))

            } else {

            }
        }
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
                    Toast.makeText(requireContext(), "Payment approved", Toast.LENGTH_SHORT).show()
                    val email = MySharedPreferences.getInstance(requireContext()).getCustomerEmail()
//                    val line_item = LineItemm()
//                    val discond_code = DiscountCode()
//                    val order = Order(email = email!!, line_items = listOf() ,discount_codes = listOf( discond_code ))
//                    val orderData = OrderData(order)
//                    viewModel.postOrder(orderData)
                }
            }, onCancel = OnCancel {
                Log.d("MYTAG", "Buyer canceled the PayPal experience.")
                Toast.makeText(requireContext(), "Payment cancelled", Toast.LENGTH_SHORT).show()
            }, onError = OnError { errorInfo ->
                Log.d("MYTAG", "Error: $errorInfo")
                Toast.makeText(requireContext(), "Payment failure $errorInfo", Toast.LENGTH_SHORT)
                    .show()
            }
        )

    }

    fun createAlert(title: String, message: String) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton("${context?.getString(R.string.ok)}") { dialog, which ->
        }
        val alertDialog = builder.create()
        alertDialog.show()
    }

}