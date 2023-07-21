package com.example.shopify.productinfo.ui.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.example.shopify.R
import com.example.shopify.SubCategoryFragment.UI.View.SubCategoryFragmentArgs
import com.example.shopify.base.State
import com.example.shopify.databinding.FragmentProductInfoBinding
import com.example.shopify.homeFragment.Model.Repository.BrandsRepository
import com.example.shopify.homeFragment.Remote.BrandsClient
import com.example.shopify.homeFragment.UI.ViewModel.HomeViewModel.HomeViewModel
import com.example.shopify.homeFragment.UI.ViewModel.HomeViewModelFactory.HomeViewModelFactory
import com.example.shopify.productinfo.model.pojo.Reviews
import com.example.shopify.productinfo.model.repository.ProductDetailsRepository
import com.example.shopify.productinfo.remote.ProductDetailsClient
import com.example.shopify.productinfo.ui.viewmodel.ProductsDetailsViewModel
import com.example.shopify.productinfo.ui.viewmodel.ProductsDetailsViewModelFactory
import kotlinx.coroutines.launch


class ProductInfoFragment : Fragment() {

    private val args: ProductInfoFragmentArgs by navArgs()
    lateinit var productBinding : FragmentProductInfoBinding
    lateinit var productsDetailsViewModel:ProductsDetailsViewModel
    lateinit var productsDetailsViewModelFactory: ProductsDetailsViewModelFactory
    val reviews = arrayOf(
        Reviews("Bassant Mohamed","This product is stylish and versatile. It looks great with a variety of outfits and can be dressed up or down."),
        Reviews("Radwa Mohamed","The fit of this item is perfect. It's true to size and hugs my body in all the right places."),
        Reviews("Sarah Mohamed","This item is a great value for the price. It's affordable and offers high-quality materials and construction."),
        Reviews("Shrouk Mohamed","The quality of this product is impressive. It's well-made and durable, ensuring that it will last for a long time."),
        Reviews( "Noha Ahmed","This item is incredibly comfortable. The fabric is soft and breathable, and it feels great to wear.")
    )


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        productBinding =  FragmentProductInfoBinding.inflate(inflater, container, false)
        return productBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        productsDetailsViewModelFactory = ProductsDetailsViewModelFactory(ProductDetailsRepository.getInstance(ProductDetailsClient()))
        productsDetailsViewModel = ViewModelProvider(this, productsDetailsViewModelFactory)[ProductsDetailsViewModel::class.java]

        val productID = args.productID?.toString()
        productsDetailsViewModel.getProductDetailsByID(productID!!)
        lifecycleScope.launch {
            productsDetailsViewModel.product.collect { result ->
                when (result) {
                    is State.Loading -> {
                        productBinding.productInfoProgressBar.visibility = View.VISIBLE
                        productBinding.productInfoConstraint.visibility = View.GONE
                        Log.i("TAG", "onViewCreated: loaaaaaaaading")
                    }
                    is State.Success -> {
                        Log.i("TAG", "onViewCreated: successsssssss")
                        productBinding.productInfoProgressBar.visibility = View.GONE
                        productBinding.productInfoConstraint.visibility = View.VISIBLE
                        productBinding.productInfoItemName.text = result.data.product.title
                        productBinding.productInfoItemPrice.text = result.data.product.variants.get(0).price
                        productBinding.productInfoDescriptionContent.text = result.data.product.bodyHtml
                        Glide.with(requireContext())
                            .load(result.data.product.images.get(0).src)
                            .into(productBinding.productInfoImages)
                    }
                    else -> {
                        Log.i("TAG", "onViewCreated: failur")

                    }
                }
            }
        }
    }


}