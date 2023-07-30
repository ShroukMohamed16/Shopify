package com.example.shopify.address.ui.view

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
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
import com.example.shopify.address.model.Address
import com.example.shopify.address.model.GetAddressResponse
import com.example.shopify.address.model.repository.AddressRepository
import com.example.shopify.address.remote.AddressClient
import com.example.shopify.address.ui.viewmodel.AddressViewModel
import com.example.shopify.address.ui.viewmodel.AddressViewModelFactory
import com.example.shopify.base.State
import com.example.shopify.base.shipping_address
import com.example.shopify.databinding.*
import com.example.shopify.utilities.Constants
import com.example.shopify.utilities.MySharedPreferences
import com.example.shopify.utilities.checkConnectivity
import com.example.shopify.utilities.getLastLocation
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch


class AddressFragment : Fragment(), OnAddressClickListener {
    lateinit var addressBinding: FragmentAddressBinding
    lateinit var addressAdapter: AddressAdapter
    lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var addressRecyclerView: RecyclerView
    lateinit var factory: AddressViewModelFactory
    lateinit var viewModel: AddressViewModel


    lateinit var cartViewModel: CartViewModel
    lateinit var cartViewModelFactory: CartViewModelFactory
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        addressBinding = FragmentAddressBinding.inflate(inflater, container, false)
        return addressBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        factory =
            AddressViewModelFactory(AddressRepository.getInstance(AddressClient.getInstance()))
        viewModel = ViewModelProvider(this, factory)[AddressViewModel::class.java]
        cartViewModelFactory =
            CartViewModelFactory(CartRepository.getInstance(CartClient.getInstance()))
        cartViewModel = ViewModelProvider(this, cartViewModelFactory)[CartViewModel::class.java]

        addressAdapter =
            AddressAdapter(GetAddressResponse(listOf()), requireContext(), this@AddressFragment)

        addressRecyclerView = addressBinding.addressRecycler
        addressRecyclerView.adapter = addressAdapter
        addressRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        if (checkConnectivity(requireContext())){
            addressBinding.addressNoInternetConnectionConstraint.visibility=View.GONE
           // addressBinding.addressRecycler.visibility=View.VISIBLE
            viewModel.getCustomerAddress(MySharedPreferences.getInstance(requireContext()).getCustomerID()!!)
            lifecycleScope.launch {
                viewModel.getAddress.collect { result ->
                    try {

                        when (result) {
                            is State.Success -> {
                                if (result.data==null){
                                    addressBinding.addressProgressBar.visibility = View.GONE
                                    addressBinding.addressRecycler.visibility=View.GONE
                                    addressBinding.noAddressAnim.visibility=View.VISIBLE
                                    addressBinding.noaddressTV.visibility=View.VISIBLE
                                }else{
                                    if(result.data?.addresses?.size!! >0){
                                        addressBinding.addressRecycler.visibility=View.VISIBLE
                                        addressBinding.addressProgressBar.visibility = View.GONE
                                        addressAdapter.setAddressList(result.data?.addresses)
                                        addressBinding.addressRecycler.adapter = addressAdapter

                                        addressBinding.noAddressAnim.visibility=View.GONE
                                        addressBinding.noaddressTV.visibility=View.GONE
                                    }else{
                                        addressBinding.addressProgressBar.visibility = View.GONE
                                        addressBinding.addressRecycler.visibility=View.GONE
                                        addressBinding.noAddressAnim.visibility=View.VISIBLE
                                        addressBinding.noaddressTV.visibility=View.VISIBLE
                                    }
                                }


                            }
                            is State.Loading -> {
                                addressBinding.addressProgressBar.visibility = View.VISIBLE
                                addressBinding.noAddressAnim.visibility=View.GONE
                                addressBinding.noaddressTV.visibility=View.GONE
                                Log.i("TAG", "onViewCreated: loading")
                            }
                            is State.Failure ->
                                Log.i("TAG", "onViewCreated: error")
                            else -> {}
                        }
                    }catch (e: Exception) {
                        Log.e("address", "Error: ${e.message}")
                    }


                }
            }
        }else{

            addressBinding.noAddressAnim.visibility=View.GONE
            addressBinding.noaddressTV.visibility=View.GONE
            addressBinding.addressRecycler.visibility=View.GONE
            addressBinding.addressProgressBar.visibility=View.GONE
            addressBinding.addressNoInternetConnectionConstraint.visibility=View.VISIBLE
        }
        addressBinding.addressFAB.setOnClickListener {
            if (checkConnectivity(requireContext())){
                displayLocationDialog()
            }else{
                Snackbar.make(view!!, R.string.no_network_connection, Snackbar.LENGTH_LONG)
                    .show()
            }
        }
    }

    private fun displayLocationDialog() {
        val builder = AlertDialog.Builder(requireContext())
        val locationDialog = AddressDialogBinding.inflate(layoutInflater)
        builder.setView(locationDialog.root)
        val dialog = builder.create()
        dialog.show()
        locationDialog.settingLocationGpsRadioButton.setOnClickListener {
            getLastLocation(
                requireContext()
            ) { _ -> }
        }
        locationDialog.settingLocationMapRadioButton.setOnClickListener { }
        locationDialog.addressOkBtn.setOnClickListener {
            if (locationDialog.settingLocationGpsRadioButton.isChecked) {
                getLastLocation(requireContext()) { _ -> }
                dialog.dismiss()
                Navigation.findNavController(requireView())
                    .navigate(R.id.action_addressFragment_to_addAddressFragment)
            } else {
                dialog.dismiss()
                if (checkConnectivity(requireContext())){
                    Navigation.findNavController(requireView())
                        .navigate(R.id.action_addressFragment_to_mapsFragment)
                }else{
                    Snackbar.make(view!!, R.string.no_network_connection, Snackbar.LENGTH_LONG)
                        .show()
                }
            }
        }

    }


    override fun onAddressDeleteListener(id: Long, address_id: Long) {
        if (checkConnectivity(requireContext())){
            viewModel.deleteCustomerAddress(id, address_id)
        }else{
            Snackbar.make(view!!, R.string.no_network_connection, Snackbar.LENGTH_LONG)
                .show()
        }
    }

    override fun onAddressCardClickListener(address: Address) {
        if (MySharedPreferences.getInstance(requireContext()).getAddressDestination()==Constants.PAYMENT_ADDRESS_DESTINATION){
            if (checkConnectivity(requireContext())){
                lifecycleScope.launch {
                    cartViewModel.getCartDraftOrderById(MySharedPreferences.getInstance(requireContext()).getCartID().toString())
                    cartViewModel.getCart.collect { result ->
                        when (result) {
                            is State.Success -> {
                                var draftOrderResponse = result.data
                                var shippingAddress = shipping_address(address1 = address.address1, address2 = address.address2, city = address.city, country = address.country, province = address.province, zip = address.zip, phone = address.phone)
                                Log.i("shippingAddress", "onAddressCardClickListener: $shippingAddress")
                                draftOrderResponse.draft_order!!.shipping_address=shippingAddress
                                Log.i("shippingAddress", "draftOrderResponse: $draftOrderResponse")

                                cartViewModel.editCartDraftOrderById(
                                    MySharedPreferences.getInstance(requireContext()).getCartID().toString(),
                                    draftOrderResponse
                                )

                                addressBinding.addressProgressBar.visibility=View.GONE
                                Navigation.findNavController(requireView())
                                    .navigate(R.id.action_addressFragment_to_paymentFragment)
                            }

                            is State.Loading -> {
                                addressBinding.addressProgressBar.visibility=View.VISIBLE
                            }
                            is State.Failure -> {

                                addressBinding.addressProgressBar.visibility=View.GONE
                            }
                        }
                    }
                }
            }else{
                Snackbar.make(view!!, R.string.no_network_connection, Snackbar.LENGTH_LONG)
                    .show()
            }
        }
    }
}