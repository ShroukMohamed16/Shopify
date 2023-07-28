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
import com.example.shopify.utilities.Constants
import com.example.shopify.utilities.MySharedPreferences
import com.example.shopify.utilities.checkConnectivity
import com.example.shopify.utilities.setAppLanguage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CartFragment : Fragment(), OnCartClickListener {

    lateinit var cartViewModel: CartViewModel
    lateinit var cartViewModelFactory: CartViewModelFactory
    lateinit var cartBinding: FragmentCartBinding
    lateinit var cartAdapter: CartAdapter
    lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var cartRecyclerView: RecyclerView
    lateinit var draftOrderResponse: DraftOrderResponse
    var totalPrice :Double = 0.0

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
            cartViewModel.getCartDraftOrderById(
                MySharedPreferences.getInstance(requireContext()).getCartID().toString()
            )
            lifecycleScope.launch {
                cartViewModel.getCart.collect { result ->
                    when (result) {
                        is State.Success -> {
                            if (result.data.draft_order!!.line_items.size == 1) {
                                cartBinding.noCartTv.visibility = View.VISIBLE
                                cartBinding.NoCartIcon.visibility = View.VISIBLE
                                cartBinding.cartRecycler.visibility = View.GONE
                                cartBinding.totalPriceTV.visibility = View.GONE
                                cartBinding.checkoutBtn.visibility = View.GONE
                            } else {
                                draftOrderResponse = result.data
                                cartBinding.cartRecycler.visibility = View.VISIBLE
                                cartBinding.noCartTv.visibility = View.GONE
                                cartBinding.NoCartIcon.visibility = View.GONE
                                cartBinding.cartProgressBar.visibility = View.GONE
                                cartBinding.totalPriceTV.visibility = View.VISIBLE
                                cartBinding.checkoutBtn.visibility = View.VISIBLE
                                cartAdapter.setCartList(result.data.draft_order!!.line_items)
                                val line = draftOrderResponse.draft_order?.line_items!!
                                var totalPrice = 0.0
                                for (i in 1 until draftOrderResponse.draft_order?.line_items!!.size) {
                                    var price = line[i].price?.toDouble()
                                    Log.i("price", "onViewCreated: price $i $price")
                                    var quantity = line[i].quantity!!
                                    totalPrice += (price!!.times(quantity))

                                    Log.i("price", "onViewCreated: total price $i $totalPrice")

                                }

                                MySharedPreferences.getInstance(requireContext()).saveTotalPrice(totalPrice.toString())
                                //cartBinding.totalPriceTV.text=totalPrice.toString()
                                var totalPricex = formatDecimal(MySharedPreferences.getInstance(requireContext()).getExchangeRate() * totalPrice)
                                cartBinding.totalPriceTV.text="price: $totalPricex ${MySharedPreferences.getInstance(requireContext()).getCurrencyCode()}"
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
                }
            }

        } else {
            Toast.makeText(requireContext(), R.string.no_internet_connection, Toast.LENGTH_SHORT)
                .show()
        }
        cartBinding.checkoutBtn.setOnClickListener {
            Navigation.findNavController(requireView())
                .navigate(R.id.action_cartFragment_to_paymentFragment)
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
            cartAdapter.setCartList(draftOrderResponse.draft_order!!.line_items)
        }
        deleteCartDialog.dialogNoBtn.setOnClickListener {
            dialog.dismiss()
        }

    }

    override fun onCartIncreaseItemClickListener(inventoryQuantity: Int) {
        TODO("Not yet implemented")
    }

    override fun onCartDecreaseItemClickListener(inventoryQuantity: Int) {
        TODO("Not yet implemented")
    }


}