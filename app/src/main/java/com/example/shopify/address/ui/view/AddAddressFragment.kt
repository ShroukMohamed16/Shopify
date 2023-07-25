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
import com.example.shopify.CategoryFragment.Model.Repository.AllCategoriesRepository
import com.example.shopify.CategoryFragment.Remote.AllCategoriesClient
import com.example.shopify.CategoryFragment.UI.ViewModel.CategoryViewModel.AllCategoriesViewModel
import com.example.shopify.CategoryFragment.UI.ViewModel.CategoryViewModelFactory.AllCategoriesViewModelFactory
import com.example.shopify.R
import com.example.shopify.address.model.Address
import com.example.shopify.address.model.repository.AddressRepository
import com.example.shopify.address.remote.AddressClient
import com.example.shopify.address.ui.viewmodel.AddressViewModel
import com.example.shopify.address.ui.viewmodel.AddressViewModelFactory
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

        val address1 = addAddressBinding.address1ET.text
        val address2 = addAddressBinding.address2ET.text
        val phone = addAddressBinding.PhoneET.text

        val address = Address(7169338474770,
            address1.toString(),
            address2.toString(),
            MyAddress.city,
            "",
            "",
            "",
            phone.toString(),
            MyAddress.province,
            MyAddress.country,
            MyAddress.zipCode,
            "",
            "",
            "",
            ""
        )
        addAddressBinding.addressSaveBtn.setOnClickListener {
            if (addAddressBinding.PhoneET.text.length != 11) {
                Toast.makeText(requireContext(), "Enter Available Number", Toast.LENGTH_SHORT)
                Log.i("TAG", "onViewCreated: Enter Available Number")

            } else if (address1.isEmpty() && address2.isEmpty() && phone.isEmpty()) {
                Toast.makeText(requireContext(), "You must fill all the fields", Toast.LENGTH_SHORT)
                Log.i("TAG", "onViewCreated: You must fill all the fields")
            } else {
                viewModel.addCustomerAddress("7169338474770", address)
                Toast.makeText(requireContext(), "Saved", Toast.LENGTH_SHORT)
                Log.i("TAG", "onViewCreated: saved")
               // lifecycleScope.launch {  }
                //viewModel.address.collect(){}

            }
            Toast.makeText(requireContext(), "pressed", Toast.LENGTH_SHORT)
        }


    }


}