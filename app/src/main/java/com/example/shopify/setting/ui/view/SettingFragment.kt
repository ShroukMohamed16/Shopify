package com.example.shopify.setting.ui.view

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.shopify.R
import com.example.shopify.databinding.AddressDialogBinding
import com.example.shopify.databinding.CurrencyDialogBinding
import com.example.shopify.databinding.FragmentHomeBinding
import com.example.shopify.databinding.FragmentSettingBinding
import com.example.shopify.databinding.LanguageDialogBinding

class SettingFragment : Fragment() {
    lateinit var settingBinding: FragmentSettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        settingBinding = FragmentSettingBinding.inflate(inflater, container, false)
        return settingBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        settingBinding.AddressCard.setOnClickListener {
            Navigation.findNavController(view)
                .navigate(R.id.action_settingFragment_to_addressFragment)
        }
        settingBinding.CurrencyCard.setOnClickListener {
            displayCurrencyDialog()
        }

        settingBinding.LanguageCard.setOnClickListener {
            displayLanguageDialog()
        }

    }
    private fun displayCurrencyDialog() {
        val builder = AlertDialog.Builder(requireContext())
        val currencyDialog = CurrencyDialogBinding.inflate(layoutInflater)
        builder.setView(currencyDialog.root)
        val dialog = builder.create()
        dialog.show()

    }
    private fun displayLanguageDialog() {
        val builder = AlertDialog.Builder(requireContext())
        val languageDialog = LanguageDialogBinding.inflate(layoutInflater)
        builder.setView(languageDialog.root)
        val dialog = builder.create()
        dialog.show()

    }

}