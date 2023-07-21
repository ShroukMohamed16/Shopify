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
import com.bumptech.glide.Glide
import com.example.shopify.base.State
import com.example.shopify.databinding.FragmentProductInfoBinding
import com.example.shopify.productinfo.model.pojo.Option
import com.example.shopify.productinfo.model.pojo.Reviews
import com.example.shopify.productinfo.model.repository.ProductDetailsRepository
import com.example.shopify.productinfo.remote.ProductDetailsClient
import com.example.shopify.productinfo.ui.viewmodel.ProductsDetailsViewModel
import com.example.shopify.productinfo.ui.viewmodel.ProductsDetailsViewModelFactory
import kotlinx.coroutines.launch

const val TAG = "ProductInfoFragment"
class ProductInfoFragment : Fragment() {

    private val args: ProductInfoFragmentArgs by navArgs()
    private lateinit var productBinding : FragmentProductInfoBinding
    private lateinit var productsDetailsViewModel:ProductsDetailsViewModel
    private lateinit var productsDetailsViewModelFactory: ProductsDetailsViewModelFactory
    lateinit var sizeAdapter: SizeAdapter
    lateinit var colorAdapter: ColorAdapter
    private var optionList:List<String> = listOf()
    private val reviews = arrayOf(
        Reviews("Bassant Mohamed","This product is stylish and versatile. It looks great with a variety of outfits and can be dressed up or down."),
        Reviews("Noha Ahmed","Disappointed with this product. The quality was poor, and it didn't work as advertised."),
        Reviews("Radwa Mohamed","The fit of this item is perfect. It's true to size and hugs my body in all the right places."),
        Reviews("Sarah Mohamed","This item is a great value for the price. It's affordable and offers high-quality materials and construction."),
        Reviews("Shrouk Mohamed","The quality of this product is impressive. It's well-made and durable, ensuring that it will last for a long time."),
        Reviews( "Ehsan Ahmed","This item is incredibly comfortable. The fabric is soft and breathable, and it feels great to wear."),
        Reviews("Sabah Mahmoud","Love this skirt! The design is unique and eye-catching, and the material is soft and flowy."),
        Reviews("Mohamed Tawfik","I was disappointed with the quality of this Product")

        )


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{

        productBinding =  FragmentProductInfoBinding.inflate(inflater, container, false)
        return productBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

         reviews.shuffle()

        colorAdapter = ColorAdapter(optionList)
        sizeAdapter = SizeAdapter(optionList)


        productsDetailsViewModelFactory = ProductsDetailsViewModelFactory(ProductDetailsRepository.getInstance(ProductDetailsClient()))
        productsDetailsViewModel = ViewModelProvider(this, productsDetailsViewModelFactory)[ProductsDetailsViewModel::class.java]

        val productID = args.productID
        productsDetailsViewModel.getProductDetailsByID(productID!!)
        lifecycleScope.launch {
            productsDetailsViewModel.product.collect { result ->
                when (result) {
                    is State.Loading -> {
                        productBinding.productInfoProgressBar.visibility = View.VISIBLE
                        productBinding.productInfoConstraint.visibility = View.GONE
                        Log.i("TAG", "onViewCreated: loading")
                    }
                    is State.Success -> {
                        Log.i("TAG", "onViewCreated: success")
                        productBinding.productInfoProgressBar.visibility = View.GONE
                        productBinding.productInfoConstraint.visibility = View.VISIBLE
                        productBinding.productInfoItemName.text = result.data.product!!.title
                        productBinding.productInfoItemPrice.text = result.data.product!!.variants!![0].price + "EGP"
                        productBinding.productInfoDescriptionContent.text = result.data.product!!.bodyHtml
                        productBinding.firstReviewerName.text = reviews[0].name
                        productBinding.firstReviewerComment.text = reviews[0].comment
                        productBinding.secondReviewerName.text = reviews[1].name
                        productBinding.secondReviewerComment.text = reviews[1].comment
                        productBinding.thirdReviewerName.text = reviews[2].name
                        productBinding.thirdReviewerComment.text = reviews[2].comment

                        colorAdapter.setColorList(result.data.product!!.options!!.get(1).values)
                        sizeAdapter.setSizeList(result.data.product!!.options!!.get(0).values)
                        productBinding.productInfoSizeRv.adapter = sizeAdapter
                        productBinding.productInfoColorRv.adapter = colorAdapter

                        Glide.with(requireContext())
                            .load(result.data.product!!.images!![0].src)
                            .into(productBinding.productInfoImages)
                    }
                    else -> {
                        Log.i("TAG", "onViewCreated: fail")

                    }
                }
            }
        }
        productBinding.showMoreTxt.setOnClickListener {
            productBinding.fourthReview.visibility = View.VISIBLE
            productBinding.fifthReview.visibility = View.VISIBLE
            productBinding.fourthReviewerName.text = reviews[3].name
            productBinding.fourthReviewerComment.text = reviews[3].comment
            productBinding.fifthReviewerName.text = reviews[4].name
            productBinding.fifthReviewerComment.text = reviews[4].comment
            productBinding.showMoreTxt.visibility = View.GONE
            productBinding.showLessTxt.visibility = View.VISIBLE

        }
        productBinding.showLessTxt.setOnClickListener {
            productBinding.fourthReview.visibility = View.GONE
            productBinding.fifthReview.visibility = View.GONE
            productBinding.showMoreTxt.visibility = View.VISIBLE
            productBinding.showLessTxt.visibility = View.GONE

        }
    }


}