package com.example.shopify.productinfo.ui.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.example.shopify.R
import com.example.shopify.SubCategoryFragment.UI.View.SubCategoryFragmentArgs
import com.example.shopify.databinding.FragmentProductInfoBinding


class ProductInfoFragment : Fragment() {
    private val args: ProductInfoFragmentArgs by navArgs()
    lateinit var productBinding : FragmentProductInfoBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_product_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val productID = args.productID?.toLong()
    }

}