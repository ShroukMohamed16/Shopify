package com.example.shopify.address.ui.view

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.shopify.R
import com.example.shopify.databinding.AddressDialogBinding
import com.example.shopify.databinding.CopounDialogBinding
import com.example.shopify.databinding.FragmentAddressBinding
import com.example.shopify.databinding.FragmentProfileBinding
import com.example.shopify.utilities.MySharedPreferences


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

    }
}