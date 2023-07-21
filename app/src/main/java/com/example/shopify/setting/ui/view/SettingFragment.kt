package com.example.shopify.setting.ui.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.shopify.R
import com.example.shopify.databinding.FragmentHomeBinding
import com.example.shopify.databinding.FragmentSettingBinding

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

    }

}