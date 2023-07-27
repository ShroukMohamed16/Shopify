package com.example.shopify.ProfileFragment.UI.View

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.shopify.R
import com.example.shopify.authentication.ui.view.AuthenticationActivity
import com.example.shopify.databinding.FragmentProfileBinding
import com.example.shopify.utilities.MySharedPreferences
import com.google.firebase.auth.FirebaseAuth


class ProfileFragment : Fragment() {
    val auth = FirebaseAuth.getInstance()
    lateinit var profileBinding: FragmentProfileBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        profileBinding = FragmentProfileBinding.inflate(inflater, container, false)
        return profileBinding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        profileBinding.profileNameTV.text = "${
            MySharedPreferences.getInstance(requireContext()).getCustomerFirstName()
        }" +" "+ "${MySharedPreferences.getInstance(requireContext()).getCustomerLastName()}"


        profileBinding.profileSettingIcon.setOnClickListener {
            Navigation.findNavController(view)
                .navigate(R.id.action_profileFragment_to_settingFragment)
        }
        profileBinding.profileLogoutBtn.setOnClickListener {

            val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
            builder.setCancelable(true)
            builder.setTitle("sign out")
            builder.setMessage(R.string.are_you_sure)
            builder.setPositiveButton(android.R.string.ok) { _, _ ->
                auth.signOut()
                val intent = Intent(requireContext(), AuthenticationActivity::class.java)
                startActivity(intent)

            }
            builder.setNegativeButton(android.R.string.cancel) { _, _ -> }
            builder.show()

        }
        profileBinding.profileMoreOrdersTv2.setOnClickListener {
            Navigation.findNavController(view)
                .navigate(R.id.action_profileFragment_to_allOrdersFragment)
        }

        profileBinding.profileMoreOrdersTv.setOnClickListener {
            Navigation.findNavController(view)
                .navigate(R.id.action_profileFragment_to_favouriteFragment)
        }

    }
}