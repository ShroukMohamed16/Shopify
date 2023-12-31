package com.example.shopify.setting.ui.view

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shopify.R
import com.example.shopify.base.State
import com.example.shopify.databinding.AddressDialogBinding
import com.example.shopify.databinding.CurrencyDialogBinding
import com.example.shopify.databinding.FragmentHomeBinding
import com.example.shopify.databinding.FragmentSettingBinding
import com.example.shopify.databinding.LanguageDialogBinding
import com.example.shopify.setting.Model.SettingRepository.SettingRepository
import com.example.shopify.setting.Remote.CurrencyClient
import com.example.shopify.setting.ui.viewModel.SettingViewModel
import com.example.shopify.setting.ui.viewModel.SettingViewModelFactory
import com.example.shopify.utilities.Constants
import com.example.shopify.utilities.MySharedPreferences
import com.example.shopify.utilities.checkConnectivity
import com.example.shopify.utilities.setAppLanguage
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class SettingFragment : Fragment(), OnClickCurrency {
    lateinit var viewModel: SettingViewModel
    lateinit var factory: SettingViewModelFactory
    private var currencyList: List<List<String>> = emptyList()
    lateinit var settingBinding: FragmentSettingBinding
    lateinit var currencyAdapter: CurrencyAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        settingBinding = FragmentSettingBinding.inflate(inflater, container, false)
        return settingBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        currencyAdapter = CurrencyAdapter(currencyList, this)
        factory = SettingViewModelFactory(SettingRepository.getInstance(CurrencyClient()))
        viewModel = ViewModelProvider(this, factory).get(SettingViewModel::class.java)
        settingBinding.currencyTV.text =
            MySharedPreferences.getInstance(requireContext()).getCurrencyCode()
        if(checkConnectivity(requireContext())){

            viewModel.getAllCurrencies()
            lifecycleScope.launch(Dispatchers.IO) {
                viewModel.currency.collect { result ->
                    when (result) {
                        is State.Loading -> {

                        }
                        is State.Success -> {

                            if (result.data.supported_codes.isEmpty()) {
                                Toast.makeText(requireContext(), "loaaading", Toast.LENGTH_SHORT).show()
                            } else {
                                currencyList = result.data.supported_codes
                            }
                            Log.i("TAG", "onViewCreated is currency setting : $currencyList")

                        }
                        else -> {
                            Log.i("TAG", "onViewCreated: failur")


                        }
                    }

                }
            }

            lifecycleScope.launch {
                viewModel.changeCurrency.collect { result ->
                    when (result) {
                        is State.Loading -> {

                        }
                        is State.Success -> {
                            val rate = result.data.conversion_rate
                            MySharedPreferences.getInstance(requireContext())
                                .saveExchangeRate(rate.toFloat())
                            Log.i("TAG", "onViewCreated: ${rate.toFloat()}")

                        }
                        else -> {

                        }
                    }

                }
            }
        }

        settingBinding.AddressCard.setOnClickListener {
            if (checkConnectivity(requireContext())){
                Navigation.findNavController(view)
                    .navigate(R.id.action_settingFragment_to_addressFragment)
                MySharedPreferences.getInstance(requireContext()).saveAddressDestination(Constants.SETTING_ADDRESS_DESTINATION)
            }
            else{
                Snackbar.make(view!!, R.string.no_network_connection, Snackbar.LENGTH_LONG)
                    .show()
            }
        }
        settingBinding.CurrencyCard.setOnClickListener {
            if (checkConnectivity(requireContext())){
                displayCurrencyDialog()
            }
            else{
                Snackbar.make(view!!, R.string.no_network_connection, Snackbar.LENGTH_LONG)
                    .show()
            }
        }

        settingBinding.LanguageCard.setOnClickListener {
            displayLanguageDialog()
        }

    }

    private fun displayCurrencyDialog() {
        val builder = AlertDialog.Builder(requireContext())
        val currencyDialog = CurrencyDialogBinding.inflate(layoutInflater)
        currencyAdapter.setCurrencyList(currencyList)
        currencyDialog.currencyRV.adapter = currencyAdapter
        builder.setView(currencyDialog.root)

        val dialog = builder.create()
        dialog.show()
        currencyDialog.okBTN.setOnClickListener {
            settingBinding.currencyTV.text =
                MySharedPreferences.getInstance(requireContext()).getCurrencyCode()
            dialog.dismiss()
        }
    }


    private fun displayLanguageDialog() {
        val builder = AlertDialog.Builder(requireContext())
        val languageDialog = LanguageDialogBinding.inflate(layoutInflater)
        builder.setView(languageDialog.root)
        val dialog = builder.create()
        dialog.show()
        when (MySharedPreferences.getInstance(requireContext()).getLanguagePreference()) {
            Constants.ENGLISH -> languageDialog.settingLanguageEnglishRadioButton.isChecked = true
            Constants.ARABIC -> languageDialog.settingLanguageArabicRadioButton.isChecked = true
        }
        languageDialog.settingLanguageArabicRadioButton.setOnClickListener {
            MySharedPreferences.getInstance(requireContext())
                .saveLanguagePreference(Constants.ARABIC)
        }
        languageDialog.settingLanguageEnglishRadioButton.setOnClickListener {
            MySharedPreferences.getInstance(requireContext())
                .saveLanguagePreference(Constants.ENGLISH)
        }
        languageDialog.settingLanguageSaveBtn.setOnClickListener {
            setAppLanguage(
                MySharedPreferences.getInstance(requireContext()).getLanguagePreference()!!
            )
            dialog.dismiss()
        }

    }

    override fun changeCurrency(code: String) {
        viewModel.changeCurrency(code)
        MySharedPreferences.getInstance(requireContext()).saveCurrencyCode(code)

    }


}