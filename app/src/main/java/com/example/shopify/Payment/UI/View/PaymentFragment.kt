package com.example.shopify.Payment.UI.View

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.shopify.AllOrdersFragment.Model.*
import com.example.shopify.Payment.Model.Repository.PaymentRepository
import com.example.shopify.Payment.Remote.PaymentClient
import com.example.shopify.Payment.UI.ViewModel.PaymentViewModel
import com.example.shopify.Payment.UI.ViewModel.PaymentViewModelFactory
import com.example.shopify.R
import com.example.shopify.databinding.FragmentPaymentBinding
import com.example.shopify.utilities.MyPriceRules


class PaymentFragment : Fragment() {
    lateinit var paymentBinding: FragmentPaymentBinding
    lateinit var viewModel: PaymentViewModel
    lateinit var factory: PaymentViewModelFactory
    lateinit var discountCodeET:EditText
    lateinit var discountCodeVervicattion:ImageView
    lateinit var couponList:MutableList<String>
    lateinit var productFeesTV:TextView
    lateinit var deliveryFeesTv:TextView
    lateinit var discountFeesTv:TextView
    lateinit var totalPriceTv:TextView

    private var deliveryPrice:Float= 20f

    private var discountPrice:Float= 0f
    private var productPrice:Float= 500f

    private var totalPrice:Float= 50f


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

        totalPrice =  productPrice-deliveryPrice
        totalPrice -= discountPrice
        discountCodeET = paymentBinding.discountCodeET
        discountCodeVervicattion = paymentBinding.verfyIcon
        productFeesTV=paymentBinding.productFees
        deliveryFeesTv=paymentBinding.deliveryFeesET
        discountFeesTv=paymentBinding.DiscountValueET
        totalPriceTv=paymentBinding.totalPriceTV

        couponList = mutableListOf<String>()
        for (i in 0 until MyPriceRules.rules.size) {
            couponList.add(MyPriceRules.rules[i].title)
        }


        productFeesTV.text= "${getString(R.string.product_fees)}$productPrice"
        deliveryFeesTv.text=  "${getString(R.string.delivery_fees)}$deliveryPrice"
        discountFeesTv.text="${getString(R.string.discount)}$discountPrice"
        totalPriceTv.text= totalPrice.toString()

        discountCodeET.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Check if the entered discount code is valid
                val code = s.toString()
                if (couponList.contains(code)) {
                    val checkmark = resources.getDrawable(R.drawable.true_copoun)
                    checkmark.setBounds(0, 0, checkmark.intrinsicWidth, checkmark.intrinsicHeight)
                    discountCodeVervicattion.setImageDrawable(checkmark)
                    discountCodeVervicattion.visibility = View.VISIBLE
                    val animator = ObjectAnimator.ofFloat(discountCodeVervicattion, "alpha", 0f, 1f)
                    animator.duration = 1000
                    animator.start()

                    discountCodeET.isEnabled = false
                } else {
                    val cross = resources.getDrawable(R.drawable.false_copoun)
                    cross.setBounds(0, 0, cross.intrinsicWidth, cross.intrinsicHeight)
                    discountCodeVervicattion.setImageDrawable(cross)
                    discountCodeVervicattion.visibility = View.VISIBLE
                    val animator = ObjectAnimator.ofFloat(discountCodeVervicattion, "alpha", 0f, 1f)
                    animator.duration = 1000
                    animator.start()
                }
            }

            override fun afterTextChanged(s: Editable?) {
                discountPrice = if (discountCodeET.text.toString()=="Summer60"){
                    productPrice-60f
                }else if (discountCodeET.text.toString()=="Buy1Get1"){
                    productPrice-productPrice
                }else{
                    0f
                }
                productFeesTV.text= "${getString(R.string.product_fees)} $productPrice"
                deliveryFeesTv.text=  "${getString(R.string.delivery_fees)} $deliveryPrice"
                discountFeesTv.text="${getString(R.string.discount)} $discountPrice"
                totalPriceTv.text= totalPrice.toString()
            }
        })
        if (paymentBinding.cashPaymentRadioButton.isEnabled){
           paymentBinding.checkoutBtn.visibility=View.VISIBLE/*
            paymentBinding.paymentButtonContainer.visibility=View.GONE*/
        }else if(paymentBinding.onlinePaymentRadioButton.isEnabled){
            paymentBinding.checkoutBtn.visibility=View.GONE/*
            paymentBinding.paymentButtonContainer.visibility=View.VISIBLE*/
        }
        paymentBinding.checkoutBtn.setOnClickListener {
            if (paymentBinding.cashPaymentRadioButton.isChecked){
            }else{

            }
        }

    }

}