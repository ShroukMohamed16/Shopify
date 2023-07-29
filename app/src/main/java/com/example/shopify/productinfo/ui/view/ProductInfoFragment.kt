package com.example.shopify.productinfo.ui.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.example.shopify.Payment.Model.DataClass.LineItem
import com.example.shopify.R
import com.example.shopify.CartFragment.UI.viewmodel.CartViewModel
import com.example.shopify.CartFragment.UI.viewmodel.CartViewModelFactory
import com.example.shopify.CartFragment.model.CartRepository
import com.example.shopify.CartFragment.remote.CartClient
import com.example.shopify.base.*
import com.example.shopify.databinding.FragmentProductInfoBinding
import com.example.shopify.productinfo.model.pojo.Product
import com.example.shopify.productinfo.model.pojo.Reviews
import com.example.shopify.productinfo.model.pojo.Variant
import com.example.shopify.productinfo.model.repository.ProductDetailsRepository
import com.example.shopify.productinfo.remote.ProductDetailsClient
import com.example.shopify.productinfo.ui.viewmodel.ProductsDetailsViewModel
import com.example.shopify.productinfo.ui.viewmodel.ProductsDetailsViewModelFactory
import com.example.shopify.utilities.MySharedPreferences
import com.example.shopify.utilities.createAlert
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.DecimalFormat

const val TAG = "ProductInfoFragment"

class ProductInfoFragment : Fragment(),OnProductVariantClickListener {

    lateinit var cartViewModel: CartViewModel
    lateinit var cartViewModelFactory: CartViewModelFactory
    private val args: ProductInfoFragmentArgs by navArgs()
    private lateinit var productBinding: FragmentProductInfoBinding
    private lateinit var productsDetailsViewModel: ProductsDetailsViewModel
    private lateinit var productsDetailsViewModelFactory: ProductsDetailsViewModelFactory
    lateinit var variantAdapter: VariantAdapter
    private var currentPage = 0
    private var variantList: List<Variant> = listOf()
    lateinit var product: Product
    var property = Property("","")
    var lineItemExists = false

    private val reviews = arrayOf(
        Reviews("Bassant Mohamed",
            "This product is stylish and versatile. It looks great with a variety of outfits and can be dressed up or down."),
        Reviews("Noha Ahmed",
            "Disappointed with this product. The quality was poor, and it didn't work as advertised."),
        Reviews("Radwa Mohamed",
            "The fit of this item is perfect. It's true to size and hugs my body in all the right places."),
        Reviews("Sarah Mohamed",
            "This item is a great value for the price. It's affordable and offers high-quality materials and construction."),
        Reviews("Shrouk Mohamed",
            "The quality of this product is impressive. It's well-made and durable, ensuring that it will last for a long time."),
        Reviews("Ehsan Ahmed",
            "This item is incredibly comfortable. The fabric is soft and breathable, and it feels great to wear."),
        Reviews("Sabah Mahmoud",
            "Love this skirt! The design is unique and eye-catching, and the material is soft and flowy."),
        Reviews("Mohamed Tawfik", "I was disappointed with the quality of this Product")

    )
    lateinit var cartProduct:Product
    lateinit var cartImageProperty:Property
    var cartLineItemList = mutableListOf<line_items>()
    private var variantID:Long = 0
    private var varientPosition:Int =0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        productBinding = FragmentProductInfoBinding.inflate(inflater, container, false)
        return productBinding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cartViewModelFactory =
            CartViewModelFactory(CartRepository.getInstance(CartClient.getInstance()))
        cartViewModel = ViewModelProvider(this, cartViewModelFactory)[CartViewModel::class.java]

        reviews.shuffle()

        variantAdapter = VariantAdapter(variantList, this)


        productsDetailsViewModelFactory =
            ProductsDetailsViewModelFactory(ProductDetailsRepository.getInstance(
                ProductDetailsClient()))
        productsDetailsViewModel = ViewModelProvider(this,
            productsDetailsViewModelFactory)[ProductsDetailsViewModel::class.java]

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
                        if(MySharedPreferences.getInstance(requireContext()).getISGuest())
                        {
                            productBinding.productInfoAddToFavoriteIcon.visibility=View.GONE
                            productBinding.productInfoAddToShoppingCartIcon.visibility=View.GONE
                        }
                        cartProduct = result.data.product!!
                        val price = result.data.product?.variants!![0]?.price
                        product = result.data.product!!
                        productBinding.productInfoProgressBar.visibility = View.GONE
                        productBinding.productInfoConstraint.visibility = View.VISIBLE
                        productBinding.productInfoItemName.text = result.data.product!!.title
                        productBinding.productInfoItemPrice.text =
                            formatDecimal(price!!.toDouble() * MySharedPreferences
                                .getInstance(requireContext()).getExchangeRate()) + " " + "${
                                MySharedPreferences.getInstance(requireContext()).getCurrencyCode()
                            }"
                        productBinding.productInfoDescriptionContent.text =
                            result.data.product!!.bodyHtml
                        productBinding.firstReviewerName.text = reviews[0].name
                        productBinding.firstReviewerComment.text = reviews[0].comment
                        productBinding.secondReviewerName.text = reviews[1].name
                        productBinding.secondReviewerComment.text = reviews[1].comment
                        productBinding.thirdReviewerName.text = reviews[2].name
                        productBinding.thirdReviewerComment.text = reviews[2].comment

                        variantAdapter.setSizeList(result.data.product!!.variants)
                        productBinding.productInfoItemsRv.adapter = variantAdapter

                        val images = result.data.product?.images
                        val countOfImages = result.data.product?.images?.size
                        val adapter = ImagesAdapter(images!!)
                        productBinding.productInfoViewPager.adapter = adapter

                        productBinding.productInfoViewPager.orientation =
                            ViewPager2.ORIENTATION_HORIZONTAL


                        productBinding.productInfoViewPager.registerOnPageChangeCallback(object :
                            ViewPager2.OnPageChangeCallback() {
                            override fun onPageSelected(position: Int) {
                            }
                        })
                        val handler = Handler(Looper.getMainLooper())
                        handler.postDelayed(
                            object : Runnable {
                                override fun run() {
                                    if (currentPage == countOfImages) {
                                        currentPage = 0
                                    } else {
                                        currentPage++
                                    }
                                    productBinding.productInfoViewPager.setCurrentItem(currentPage,
                                        true)
                                    handler.postDelayed(this,
                                        5000) // Change the delay time as needed
                                }
                            }, 5000)
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

        productBinding.productInfoAddToFavoriteIcon.setOnClickListener {

            productBinding.productInfoAddToFavoriteIcon.setImageResource(R.drawable.fill_favorite)

            productsDetailsViewModel.getDraftOrder(MySharedPreferences.getInstance(requireContext()).getFavID()!!)
            lifecycleScope.launch {
                productsDetailsViewModel.draftOrderInfo.collect { result ->
                    when (result) {
                        is State.Loading -> {
                            Toast.makeText(context, "Loading", Toast.LENGTH_LONG).show()
                        }
                        is State.Success -> {
                            property.name = product.image!!.src
                            Log.i(TAG, "onViewCreated product image: ${product.image!!.src}")
                            val lineItem = line_items(
                                title = product.title!!,
                                quantity = 1,
                                price = product.variants!!.get(0).price!!,
                                variant_id = product.variants!!.get(0).id!!,
                                product_id = product.id!!,
                                properties = arrayListOf(property)
                            )

                            for (existingLineItem in result.data.draft_order!!.line_items) {
                                if (existingLineItem.title == lineItem.title
                                    && existingLineItem.quantity == lineItem.quantity
                                    && existingLineItem.price == lineItem.price
                                    && existingLineItem.variant_id == lineItem.variant_id
                                    && existingLineItem.product_id == lineItem.product_id
                                    && existingLineItem.properties == lineItem.properties
                                ) {
                                    lineItemExists = true
                                    break
                                }
                            }
                            if(!lineItemExists) {
                                var oldLineItemsList = result.data.draft_order!!.line_items
                                var newLineItem = lineItem
                                var updatedLineItem = oldLineItemsList + newLineItem

                                var draft_order = DraftOrderResponse(
                                    DraftOrder(
                                        email = "",
                                        line_items = updatedLineItem
                                    )
                                )
                                productsDetailsViewModel.modifyDraftOrder(
                                    MySharedPreferences.getInstance(
                                        requireContext()
                                    ).getFavID()!!, draft_order
                                )
                            }else{
                                productBinding.productInfoAddToFavoriteIcon.setImageResource(R.drawable.fill_favorite)
                                val builder = AlertDialog.Builder(requireContext())
                                builder.setMessage("This Item Already Exist")
                                builder.setPositiveButton("Ok") { dialog, it ->
                                    dialog.dismiss()
                                }

                                val dialog = builder.create()
                                dialog.show()
                            }

                        }
                        is State.Failure -> {
                            Toast.makeText(
                                requireContext(),
                                "Fail to get Draft Order",
                                Toast.LENGTH_LONG
                            )
                                .show()

                        }

                    }

                }
            }
        }
        productBinding.productInfoAddToShoppingCartIcon.setOnClickListener {
            if (variantID == 0L) {
                createAlert(
                    getString(R.string.choose_size_color_title),
                    getString(R.string.must_choose_size_color_message),
                    requireContext()
                )
            } else {
                lifecycleScope.launch {
                    cartViewModel.getCartDraftOrderById(
                        MySharedPreferences.getInstance(
                            requireContext()
                        ).getCartID().toString()
                    )
                    cartViewModel.getCart.collect { result ->
                        when (result) {
                            is State.Success -> {
                                cartImageProperty =
                                    Property("imageUrl", cartProduct.image?.src.toString())
                                val cartInventoryQuantity = Property(
                                    "inventoryQuantity",
                                    cartProduct?.variants?.get(varientPosition)?.inventoryQuantity.toString()
                                )
                                val cartVariantPosition = Property(
                                    "cartVariantPosition",
                                    varientPosition.toString()
                                )

                                val lineItem = line_items(
                                    title = cartProduct.title!!,
                                    quantity = 1,
                                    price = cartProduct.variants?.get(varientPosition)?.price!!,
                                    variant_id = variantID,
                                    product_id = cartProduct.id!!,
                                    properties = arrayListOf(
                                        cartImageProperty,
                                        cartInventoryQuantity,
                                        cartVariantPosition
                                    )

                                )
                                var lineItemExists = false
                                for (existingLineItem in result.data.draft_order!!.line_items) {
                                    if (existingLineItem.title == lineItem.title
                                        && existingLineItem.quantity == lineItem.quantity
                                        && existingLineItem.price == lineItem.price
                                        && existingLineItem.variant_id == lineItem.variant_id
                                        && existingLineItem.product_id == lineItem.product_id
                                        && existingLineItem.properties == lineItem.properties
                                    ) {
                                        lineItemExists = true
                                        break
                                    }
                                }
                                if(!lineItemExists) {

                                    val list = result.data.draft_order?.line_items
                                    val mutableList = list?.toMutableList()
                                    mutableList?.add(lineItem)
                                    result.data.draft_order?.line_items = mutableList!!.toList()
                                    cartViewModel.editCartDraftOrderById(
                                        MySharedPreferences.getInstance(
                                            requireContext()
                                        ).getCartID().toString(), result.data
                                    )
                                }else{
                                    createAlert("already added", "this item is already added to cart", requireContext())
                                }

                                /*if (!result.data.draft_order?.line_items!!.contains(lineItem)) {
                                    val list = result.data.draft_order?.line_items
                                    val mutableList = list?.toMutableList()
                                    mutableList?.add(lineItem)
                                    result.data.draft_order?.line_items = mutableList!!.toList()
                                    cartViewModel.editCartDraftOrderById(
                                        MySharedPreferences.getInstance(
                                            requireContext()
                                        ).getCartID().toString(), result.data
                                    )
                                } else {
                                    createAlert("already added", "this item is already added to cart", requireContext())
                                }*/
                                Toast.makeText(requireContext(), "saved", Toast.LENGTH_SHORT)
                                    .show()
                            }
                            is State.Loading -> {

                                Toast.makeText(requireContext(), "Loading", Toast.LENGTH_SHORT)
                                    .show()


                            }
                            is State.Failure -> {
                                Toast.makeText(
                                    requireContext(),
                                    "failed to add to cart",
                                    Toast.LENGTH_SHORT
                                ).show()


                            }
                        }

                    }
                }
            }
        }

    }
    fun formatDecimal(decimal: Double): String {
        val decimalFormat = DecimalFormat("0.00")
        return decimalFormat.format(decimal)
    }


    override fun onProductVariantClick(variantId: Long,position:Int) {
        this.variantID = variantId
        this.varientPosition=position
        Log.i("TAG", "onProductVariantClick: $variantID")
    }
}