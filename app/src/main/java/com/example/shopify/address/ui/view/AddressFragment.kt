package com.example.shopify.address.ui.view

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.example.shopify.R
import com.example.shopify.databinding.*
import com.example.shopify.utilities.MyLocation
import com.example.shopify.utilities.MySharedPreferences
import com.example.shopify.utilities.getLastLocation
import java.util.*


class AddressFragment : Fragment() {
    lateinit var addressBinding: FragmentAddressBinding

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
        addressBinding.addressFAB.setOnClickListener {
            displayLocationDialog()
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
            if (locationDialog.settingLocationGpsRadioButton.isChecked){
                getLastLocation(requireContext()) { _ -> }
                dialog.dismiss()
                Navigation.findNavController(requireView())
                    .navigate(R.id.action_addressFragment_to_addAddressFragment)
            }else{
                dialog.dismiss()
                Navigation.findNavController(requireView())
                    .navigate(R.id.action_addressFragment_to_mapsFragment)
            }
        }

    }

}