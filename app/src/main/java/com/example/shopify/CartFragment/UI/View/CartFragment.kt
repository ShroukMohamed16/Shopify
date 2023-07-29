package com.example.shopify.CartFragment.UI.View

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shopify.CartFragment.UI.viewmodel.CartViewModel
import com.example.shopify.CartFragment.UI.viewmodel.CartViewModelFactory
import com.example.shopify.CartFragment.model.CartRepository
import com.example.shopify.CartFragment.remote.CartClient
import com.example.shopify.R
import com.example.shopify.address.model.GetAddressResponse
import com.example.shopify.address.model.repository.AddressRepository
import com.example.shopify.address.remote.AddressClient
import com.example.shopify.address.ui.view.AddressAdapter
import com.example.shopify.address.ui.viewmodel.AddressViewModel
import com.example.shopify.address.ui.viewmodel.AddressViewModelFactory
import com.example.shopify.base.DraftOrderResponse
import com.example.shopify.base.Property
import com.example.shopify.base.State
import com.example.shopify.base.line_items
import com.example.shopify.databinding.DeleteCartItemDialogBinding
import com.example.shopify.databinding.FragmentCartBinding
import com.example.shopify.databinding.LanguageDialogBinding
import com.example.shopify.productinfo.model.repository.ProductDetailsRepository
import com.example.shopify.productinfo.remote.ProductDetailsClient
import com.example.shopify.productinfo.ui.viewmodel.ProductsDetailsViewModel
import com.example.shopify.productinfo.ui.viewmodel.ProductsDetailsViewModelFactory
import com.example.shopify.utilities.*
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CartFragment : Fragment(), OnCartClickListener {

    lateinit var cartViewModel: CartViewModel
    lateinit var cartViewModelFactory: CartViewModelFactory
    private lateinit var productsDetailsViewModel: ProductsDetailsViewModel
    private lateinit var productsDetailsViewModelFactory: ProductsDetailsViewModelFactory
    lateinit var cartBinding: FragmentCartBinding
    lateinit var cartAdapter: CartAdapter
    lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var cartRecyclerView: RecyclerView
    lateinit var draftOrderResponse: DraftOrderResponse
    val productIdsList = mutableListOf<Long>()

    val variantPositionList = mutableListOf<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        cartBinding = FragmentCartBinding.inflate(inflater, container, false)
        return cartBinding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        productsDetailsViewModelFactory =
            ProductsDetailsViewModelFactory(
                ProductDetailsRepository.getInstance(
                    ProductDetailsClient()
                )
            )
        productsDetailsViewModel = ViewModelProvider(
            this,
            productsDetailsViewModelFactory
        )[ProductsDetailsViewModel::class.java]
        cartViewModelFactory =
            CartViewModelFactory(CartRepository.getInstance(CartClient.getInstance()))
        cartViewModel = ViewModelProvider(this, cartViewModelFactory)[CartViewModel::class.java]

        cartAdapter =
            CartAdapter(listOf(), requireContext(), this)

        cartRecyclerView = cartBinding.cartRecycler
        cartRecyclerView.adapter = cartAdapter
        cartRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        if (checkConnectivity(requireContext())) {

            cartBinding.noInternetConstraint.visibility = View.GONE
            cartBinding.cartItemsConstraint.visibility = View.VISIBLE
            cartViewModel.getCartDraftOrderById(
                MySharedPreferences.getInstance(requireContext()).getCartID().toString()
            )
            lifecycleScope.launch {
                cartViewModel.getCart.collect { result ->
                    try {
                        when (result) {
                            is State.Success -> {
                                if (result.data.draft_order!!.line_items.size == 1) {
                                    cartBinding.noCartItemsTxt.visibility = View.VISIBLE
                                    cartBinding.nocartAnmi.visibility = View.VISIBLE
                                    cartBinding.cartRecycler.visibility = View.GONE
                                    cartBinding.totalPriceTV.visibility = View.GONE
                                    cartBinding.checkoutBtn.visibility = View.GONE
                                } else {
                                    draftOrderResponse = result.data
                                    cartBinding.cartRecycler.visibility = View.VISIBLE
                                    cartBinding.noCartItemsTxt.visibility = View.GONE
                                    cartBinding.nocartAnmi.visibility = View.GONE
                                    cartBinding.cartProgressBar.visibility = View.GONE
                                    cartBinding.totalPriceTV.visibility = View.VISIBLE
                                    cartBinding.checkoutBtn.visibility = View.VISIBLE
                                    cartAdapter.setCartList(result.data.draft_order.line_items)
                                    calculateTotalPrice()
                                    for (i in 1 until result.data.draft_order!!.line_items.size){
                                        productIdsList.add(result.data.draft_order!!.line_items[i].product_id!!)
                                        variantPositionList.add(result.data.draft_order!!.line_items[i].properties[2].value!!.toInt())
                                    }
                                    /*for (i in 0 until productIdsList.size){
                                        productsDetailsViewModel.getProductDetailsByID(productIdsList[i].toString())
                                        productsDetailsViewModel.product.collect{ result->
                                            try {
                                                when(result){
                                                    is State.Success->{
                                                        productCountList.add(result.data.product?.variants?.get(variantPositionList[i])?.inventoryQuantity!!)

                                                        Log.i("productCount", "onViewCreated:$i ${productCountList[i]}")
                                                    }
                                                    else->{}
                                                }
                                            } catch (e: Exception) {
                                                Log.e("productCount", "Error: ${e.message}")
                                            }
                                        }
                                    }*/
                                }
                            }
                            is State.Loading -> {
                                cartBinding.cartRecycler.visibility = View.GONE
                                cartBinding.totalPriceTV.visibility = View.GONE
                                cartBinding.checkoutBtn.visibility = View.GONE
                                cartBinding.cartProgressBar.visibility = View.VISIBLE
                            }
                            is State.Failure -> {
                                cartBinding.cartProgressBar.visibility = View.GONE
                                Toast.makeText(
                                    requireContext(),
                                    "failed to load ",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    } catch (e: Exception) {
                        Log.e("cartViewModel", "Error: ${e.message}")
                    }
                }
            }

        } else {
            cartBinding.noInternetConstraint.visibility = View.VISIBLE
            cartBinding.cartItemsConstraint.visibility = View.GONE
        }
        cartBinding.checkoutBtn.setOnClickListener {
            if (checkConnectivity(requireContext())){
                MySharedPreferences.getInstance(requireContext()).saveAddressDestination(Constants.PAYMENT_ADDRESS_DESTINATION)
                Navigation.findNavController(requireView())
                    .navigate(R.id.action_cartFragment_to_addressFragment)
            }else{
                Snackbar.make(view!!, R.string.no_network_connection, Snackbar.LENGTH_LONG)
                    .show()
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onCartDeleteClickListener(lineItem: line_items) {
        val builder = AlertDialog.Builder(requireContext())
        val deleteCartDialog = DeleteCartItemDialogBinding.inflate(layoutInflater)
        builder.setView(deleteCartDialog.root)
        val dialog = builder.create()
        dialog.show()
        deleteCartDialog.dialogYesBtn.setOnClickListener {
            cartAdapter.setCartList(listOf())
            if (checkConnectivity(requireContext())){
                val list = draftOrderResponse.draft_order?.line_items
                val mutableList = list?.toMutableList()
                mutableList?.remove(lineItem)
                draftOrderResponse.draft_order?.line_items = mutableList!!.toList()
                cartViewModel.editCartDraftOrderById(
                    MySharedPreferences.getInstance(
                        requireContext()
                    ).getCartID().toString(), draftOrderResponse
                )
                dialog.dismiss()
                calculateTotalPrice()
                cartAdapter.setCartList(draftOrderResponse.draft_order!!.line_items)
            }else{
                dialog.dismiss()
                Snackbar.make(view!!, R.string.no_network_connection, Snackbar.LENGTH_LONG)
                    .show()
            }

        }
        deleteCartDialog.dialogNoBtn.setOnClickListener {
            dialog.dismiss()
        }

    }

    override fun onCartIncreaseItemClickListener(
        inventoryQuantity: Int,
        position: Int,
        currentQuantity: Int
    ) {
        var current = currentQuantity
        if (checkConnectivity(requireContext())){
            if (currentQuantity < inventoryQuantity) {
                current += 1
                draftOrderResponse.draft_order!!.line_items[position].quantity = current
                cartViewModel.editCartDraftOrderById(
                    MySharedPreferences.getInstance(requireContext()).getCartID().toString(),
                    draftOrderResponse
                )
                cartAdapter.setCartList(draftOrderResponse.draft_order!!.line_items)
                calculateTotalPrice()
            } else {
                createAlert("", "the available items finished out", requireContext())
            }
        }else{
            Snackbar.make(view!!, R.string.no_network_connection, Snackbar.LENGTH_LONG)
                .show()
        }

    }

    override fun onCartDecreaseItemClickListener(
        inventoryQuantity: Int,
        position: Int,
        currentQuantity: Int
    ) {
        var current = currentQuantity
            if (checkConnectivity(requireContext())){
                if (currentQuantity == 1) {
                    createAlert("", "can't have less than one item", requireContext())

                } else {
                    current -= 1
                    draftOrderResponse.draft_order!!.line_items[position].quantity = current
                    cartViewModel.editCartDraftOrderById(
                        MySharedPreferences.getInstance(requireContext()).getCartID().toString(),
                        draftOrderResponse
                    )
                    cartAdapter.setCartList(draftOrderResponse.draft_order!!.line_items)
                    calculateTotalPrice()
                } }else{
                Snackbar.make(view!!, R.string.no_network_connection, Snackbar.LENGTH_LONG)
                    .show()
            }
    }


    @SuppressLint("SetTextI18n")
    fun calculateTotalPrice() {
        var totalPrice = 0.0
        for (i in 1 until draftOrderResponse.draft_order?.line_items!!.size) {
            var price = draftOrderResponse.draft_order?.line_items!![i].price?.toDouble()
            Log.i("price", "onViewCreated: price $i $price")
            var quantity = draftOrderResponse.draft_order!!.line_items[i].quantity!!
            totalPrice += (price!!.times(quantity))
            Log.i("price", "onViewCreated: total price $i $totalPrice")
        }
        if (totalPrice == 0.0) {
            cartBinding.totalPriceTV.visibility = View.GONE
            cartBinding.checkoutBtn.visibility = View.GONE
            cartBinding.nocartAnmi.visibility =View.VISIBLE
            cartBinding.noCartItemsTxt.visibility=View.VISIBLE
        }
        MySharedPreferences.getInstance(requireContext()).saveTotalPrice(totalPrice.toString())
        val exchangedTotalPrice = formatDecimal(
            MySharedPreferences.getInstance(requireContext()).getExchangeRate() * totalPrice
        )
        cartBinding.totalPriceTV.text = "price: $exchangedTotalPrice ${
            MySharedPreferences.getInstance(requireContext()).getCurrencyCode()
        }"
    }
}