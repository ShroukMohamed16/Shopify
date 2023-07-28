package com.example.shopify.address.ui.view

import android.annotation.SuppressLint
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
import com.example.shopify.CategoryFragment.Model.Repository.AllCategoriesRepository
import com.example.shopify.CategoryFragment.Remote.AllCategoriesClient
import com.example.shopify.CategoryFragment.UI.ViewModel.CategoryViewModel.AllCategoriesViewModel
import com.example.shopify.CategoryFragment.UI.ViewModel.CategoryViewModelFactory.AllCategoriesViewModelFactory
import com.example.shopify.R
import com.example.shopify.address.model.Address
import com.example.shopify.address.model.AddressBody
import com.example.shopify.address.model.AddressModel
import com.example.shopify.address.model.repository.AddressRepository
import com.example.shopify.address.remote.AddressClient
import com.example.shopify.address.ui.viewmodel.AddressViewModel
import com.example.shopify.address.ui.viewmodel.AddressViewModelFactory
import com.example.shopify.base.State
import com.example.shopify.databinding.FragmentAddAddressBinding
import com.example.shopify.databinding.FragmentAddressBinding
import com.example.shopify.utilities.MyAddress
import com.example.shopify.utilities.MyLocation
import com.example.shopify.utilities.MySharedPreferences
import com.example.shopify.utilities.getAddress
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*


class AddAddressFragment : Fragment() {
    lateinit var addAddressBinding: FragmentAddAddressBinding
    lateinit var factory: AddressViewModelFactory
    lateinit var viewModel: AddressViewModel
    var address1: String = ""
    var address2: String = ""
    var phone: String = ""
    lateinit var address: AddressBody
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        addAddressBinding = FragmentAddAddressBinding.inflate(inflater, container, false)
        return addAddressBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        factory =
            AddressViewModelFactory(AddressRepository.getInstance(AddressClient.getInstance()))
        viewModel = ViewModelProvider(this, factory).get(AddressViewModel::class.java)

        getAddress(requireContext(), MyLocation.lat!!, MyLocation.lng!!)
        addAddressBinding.cityET.text =
            "City: ${MyAddress.city}"

        addAddressBinding.CountryET.text =
            "Country: ${MyAddress.country}"

        addAddressBinding.ZIPCodeET.text =
            "ZipCode: ${MyAddress.zipCode}"




        addAddressBinding.addressSaveBtn.setOnClickListener {
            address1 = addAddressBinding.address1ET.text.toString().trim()
            address2 = addAddressBinding.address2ET.text.toString().trim()
            phone = addAddressBinding.PhoneET.text.toString().trim()
            if (addAddressBinding.PhoneET.text.length != 11) {
                Toast.makeText(requireContext(), "Enter Available Number", Toast.LENGTH_SHORT)
                    .show()

            } else if (address1.isEmpty() || address2.isEmpty() || phone.isEmpty()) {
                Toast.makeText(requireContext(), "You must fill all the fields", Toast.LENGTH_SHORT)
                    .show()
            } else {

                Toast.makeText(requireContext(), "Saved", Toast.LENGTH_SHORT)
                Log.i("TAG", "onViewCreated: saved")


                val address = AddressBody(
                    AddressModel(
                        address1,
                        address2,
                        MyAddress.city!!,
                        MyAddress.country!!,
                        phone/*,
                        MyAddress.zipCode,
                        MyAddress.province*/
                    )
                )
                viewModel.addCustomerAddress(MySharedPreferences.getInstance(requireContext()).getCustomerID()!!, address)
                lifecycleScope.launch {

                    viewModel.address.collect { result ->
                        when (result) {
                            is State.Success -> {
                                Log.i("TAG", "onViewCreated: saved")
                                Navigation.findNavController(requireView())
                                    .navigate(R.id.action_addAddressFragment_to_addressFragment)
                            }
                            is State.Loading ->
                                Log.i("TAG", "onViewCreated: loading")
                            is State.Failure ->
                                Log.i("TAG", "onViewCreated: error")
                        }

                    }
                }


            }
        }


    }


}