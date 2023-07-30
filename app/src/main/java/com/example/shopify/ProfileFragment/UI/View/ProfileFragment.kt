package com.example.shopify.ProfileFragment.UI.View

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.example.shopify.AllOrdersFragment.Model.OrderRepository.AllOrderRepository
import com.example.shopify.AllOrdersFragment.Remote.AllOrderClinet
import com.example.shopify.AllOrdersFragment.UI.View.AllOrdersFragment
import com.example.shopify.AllOrdersFragment.UI.ViewModel.ALlOrderViewModel
import com.example.shopify.AllOrdersFragment.UI.ViewModel.AllOrderViewModelFactory
import com.example.shopify.CartFragment.UI.View.formatDecimal
import com.example.shopify.R
import com.example.shopify.authentication.ui.view.AuthenticationActivity
import com.example.shopify.base.State
import com.example.shopify.databinding.FragmentProfileBinding
import com.example.shopify.favourite.model.repository.FavouriteRepository
import com.example.shopify.favourite.remote.FavouriteClient
import com.example.shopify.favourite.ui.viewmodel.FavouriteViewModel
import com.example.shopify.favourite.ui.viewmodel.FavouriteViewModelFactory
import com.example.shopify.utilities.MySharedPreferences
import com.example.shopify.utilities.checkConnectivity
import com.example.shopify.utilities.convertDateTimeFormat
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch


class ProfileFragment : Fragment() {

    lateinit var favouriteViewModel: FavouriteViewModel
    lateinit var favouriteViewModelFactory: FavouriteViewModelFactory
    lateinit var viewModel: ALlOrderViewModel
    lateinit var factory: AllOrderViewModelFactory
    val auth = FirebaseAuth.getInstance()
    lateinit var profileBinding: FragmentProfileBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        profileBinding = FragmentProfileBinding.inflate(inflater, container, false)
        return profileBinding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        factory = AllOrderViewModelFactory(AllOrderRepository.getInstance(AllOrderClinet()))
        viewModel = ViewModelProvider(this, factory).get(ALlOrderViewModel::class.java)
        favouriteViewModelFactory = FavouriteViewModelFactory(
            FavouriteRepository.getInstance(
                FavouriteClient()
            )
        )
        favouriteViewModel =
            ViewModelProvider(this, favouriteViewModelFactory)[FavouriteViewModel::class.java]

        if (MySharedPreferences.getInstance(requireContext()).getISGuest()) {
            profileBinding.profileNameTV.visibility = View.GONE
            profileBinding.profileMoreOrdersTv.visibility = View.GONE
            profileBinding.profileMoreOrdersTv2.visibility = View.GONE
            profileBinding.profileSettingIcon.visibility = View.GONE
            profileBinding.profileLogoutBtn.visibility = View.VISIBLE
            profileBinding.profileNameTV.visibility = View.GONE
            profileBinding.profileFirstWishListCardView.visibility = View.GONE
            profileBinding.firstOrderCardView.visibility = View.GONE
            profileBinding.profileSecondWishListCardView.visibility = View.GONE
            profileBinding.secondOrderCardView.visibility = View.GONE
            profileBinding.profilefirstLine.visibility = View.GONE
            profileBinding.profileSecondLine.visibility = View.GONE

        }
        val first_name = MySharedPreferences.getInstance(requireContext()).getCustomerFirstName()
        val last_name = MySharedPreferences.getInstance(requireContext()).getCustomerLastName()
        Log.i("TAG", "onViewCreated: $first_name")
        Log.i("TAG", "onViewCreated: $last_name")

        profileBinding.profileNameTV.text = first_name + " " + last_name

        if (checkConnectivity(requireContext())) {
            val customerEmail = MySharedPreferences.getInstance(requireContext()).getCustomerEmail()
            viewModel.getAllOrdersOfUser(customerEmail!!)
            lifecycleScope.launch {
                viewModel.order.collect { result ->
                    when (result) {
                        is State.Loading -> {
                            profileBinding.orderProgreeBar.visibility = View.VISIBLE
                            profileBinding.firstOrderCardView.visibility = View.GONE
                            profileBinding.secondOrderCardView.visibility = View.GONE
                            profileBinding.profileMoreOrdersTv2.visibility = View.GONE
                            profileBinding.noOrdersYet.visibility = View.GONE
                            profileBinding.profileMoreOrdersTv2.isEnabled = false
                        }

                        is State.Success -> {
                            Log.i("TAG", "onViewCreated: success the order List")
                            val orderList = result.data.orders
                            if (orderList.isNullOrEmpty()) {

                                profileBinding.orderProgreeBar.visibility = View.GONE
                                profileBinding.firstOrderCardView.visibility = View.GONE
                                profileBinding.secondOrderCardView.visibility = View.GONE
                                profileBinding.profileMoreOrdersTv2.visibility = View.GONE
                                profileBinding.noOrdersYet.visibility = View.VISIBLE
                                profileBinding.profileMoreOrdersTv2.isEnabled = false
                                Log.i("TAG", "onViewCreated: null listssssssssss")
                            } else {
                                Log.i("TAG", "onViewCreated: not null listssssssssss")
                                if (orderList.size == 1) {
                                    profileBinding.profileMoreOrdersTv2.isEnabled = true
                                    profileBinding.orderProgreeBar.visibility = View.GONE
                                    profileBinding.firstOrderCardView.visibility = View.VISIBLE
                                    profileBinding.secondOrderCardView.visibility = View.GONE
                                    profileBinding.profileMoreOrdersTv2.visibility = View.VISIBLE
                                    profileBinding.noOrdersYet.visibility = View.GONE
                                    profileBinding.profileFirstOrderDateTv.text =
                                        convertDateTimeFormat(orderList[0].processed_at!!)
                                    profileBinding.profileFirstOrderPriceTv.text = formatDecimal(
                                        orderList[0].total_price!!.toDouble() * MySharedPreferences
                                            .getInstance(requireContext()).getExchangeRate()
                                    ) + " " + "${
                                        MySharedPreferences.getInstance(requireContext())
                                            .getCurrencyCode()
                                    }"

                                } else {

                                    profileBinding.profileMoreOrdersTv2.isEnabled = true
                                    profileBinding.orderProgreeBar.visibility = View.GONE
                                    profileBinding.firstOrderCardView.visibility = View.VISIBLE
                                    profileBinding.secondOrderCardView.visibility = View.VISIBLE
                                    profileBinding.profileMoreOrdersTv2.visibility = View.VISIBLE
                                    profileBinding.noOrdersYet.visibility = View.GONE
                                    profileBinding.profileFirstOrderDateTv.text =
                                        convertDateTimeFormat(orderList[0].processed_at!!)
                                    profileBinding.profileFirstOrderPriceTv.text = formatDecimal(
                                        orderList[0].total_price!!.toDouble() * MySharedPreferences
                                            .getInstance(requireContext()).getExchangeRate()
                                    ) + " " + "${
                                        MySharedPreferences.getInstance(requireContext())
                                            .getCurrencyCode()
                                    }"
                                    profileBinding.profileSecondOrderDateTv.text =
                                        convertDateTimeFormat(orderList[1].processed_at!!)
                                    profileBinding.profileSecondOrderPriceTv.text = formatDecimal(
                                        orderList[1].total_price!!.toDouble() * MySharedPreferences
                                            .getInstance(requireContext()).getExchangeRate()
                                    ) + " " + "${
                                        MySharedPreferences.getInstance(requireContext())
                                            .getCurrencyCode()
                                    }"

                                }

                            }
                        }
                        else -> {
                            profileBinding.orderProgreeBar.visibility = View.GONE
                            profileBinding.firstOrderCardView.visibility = View.GONE
                            profileBinding.secondOrderCardView.visibility = View.GONE
                            profileBinding.profileMoreOrdersTv2.visibility = View.GONE
                            profileBinding.noOrdersYet.text =
                                getString(R.string.no_network_connection)
                            profileBinding.noOrdersYet.visibility = View.VISIBLE
                            profileBinding.profileMoreOrdersTv2.isEnabled = false

                        }

                    }
                }
            }

        } else {
            profileBinding.orderProgreeBar.visibility = View.GONE
            profileBinding.firstOrderCardView.visibility = View.GONE
            profileBinding.secondOrderCardView.visibility = View.GONE
            profileBinding.profileMoreOrdersTv2.visibility = View.GONE
            profileBinding.noOrdersYet.text = getString(R.string.no_network_connection)
            profileBinding.profileMoreOrdersTv2.isEnabled = false
            profileBinding.noOrdersYet.visibility = View.VISIBLE

        }

        if (checkConnectivity(requireContext())) {
            favouriteViewModel.getFavDraftOrder(
                MySharedPreferences.getInstance(requireContext()).getFavID()!!
            )

            lifecycleScope.launch {
                favouriteViewModel.draftOrderDetailsList.collect { result ->
                    when (result) {
                        is State.Loading -> {
                            profileBinding.favProgressBar.visibility = View.VISIBLE
                            profileBinding.profileFirstWishListCardView.visibility = View.GONE
                            profileBinding.profileSecondWishListCardView.visibility = View.GONE
                            profileBinding.profileMoreOrdersTv.visibility = View.GONE
                            profileBinding.noFavouritesYet.visibility = View.GONE
                        }
                        is State.Success -> {
                            val lineItemsList = result.data.draft_order!!.line_items
                            if (result.data.draft_order!!.line_items.size == 1) {
                                profileBinding.favProgressBar.visibility = View.GONE
                                profileBinding.profileFirstWishListCardView.visibility = View.GONE
                                profileBinding.profileSecondWishListCardView.visibility = View.GONE
                                profileBinding.profileMoreOrdersTv.visibility = View.GONE
                                profileBinding.noFavouritesYet.visibility = View.VISIBLE

                            } else if (result.data.draft_order!!.line_items.size == 2) {
                                profileBinding.profileMoreOrdersTv2.isEnabled = true
                                profileBinding.favProgressBar.visibility = View.GONE
                                profileBinding.profileFirstWishListCardView.visibility =
                                    View.VISIBLE
                                profileBinding.profileSecondWishListCardView.visibility = View.GONE
                                profileBinding.profileMoreOrdersTv.visibility = View.VISIBLE
                                profileBinding.noFavouritesYet.visibility = View.GONE

                                profileBinding.profileFirstWishListItemName.text =
                                    lineItemsList[1].title
                                Glide.with(requireContext())
                                    .load(lineItemsList[1].properties.get(0).name.toString())
                                    .placeholder(R.drawable.ic_launcher_foreground)
                                    .error(R.drawable.ic_launcher_background)
                                    .into(profileBinding.profileFirstWishListItemImage)
                                profileBinding.profileFirstWishListItemPrice.text = formatDecimal(
                                    lineItemsList[1].price!!.toDouble() * MySharedPreferences
                                        .getInstance(requireContext()).getExchangeRate()
                                ) + " " + "${
                                    MySharedPreferences.getInstance(requireContext())
                                        .getCurrencyCode()
                                }"


                            } else if (result.data.draft_order!!.line_items.size > 2) {
                                profileBinding.profileMoreOrdersTv.isEnabled = true
                                profileBinding.favProgressBar.visibility = View.GONE
                                profileBinding.profileFirstWishListCardView.visibility =
                                    View.VISIBLE
                                profileBinding.profileSecondWishListCardView.visibility =
                                    View.VISIBLE
                                profileBinding.profileMoreOrdersTv.visibility = View.VISIBLE
                                profileBinding.noFavouritesYet.visibility = View.GONE

                                profileBinding.profileFirstWishListItemName.text =
                                    lineItemsList[1].title
                                Glide.with(requireContext())
                                    .load(lineItemsList[1].properties.get(0).name.toString())
                                    .placeholder(R.drawable.ic_launcher_foreground)
                                    .error(R.drawable.ic_launcher_background)
                                    .into(profileBinding.profileFirstWishListItemImage)
                                profileBinding.profileFirstWishListItemPrice.text = formatDecimal(
                                    lineItemsList[1].price!!.toDouble() * MySharedPreferences
                                        .getInstance(requireContext()).getExchangeRate()
                                ) + " " + "${
                                    MySharedPreferences.getInstance(requireContext())
                                        .getCurrencyCode()
                                }"

                                profileBinding.profileSecondWishListItemName.text =
                                    lineItemsList[2].title
                                Glide.with(requireContext())
                                    .load(lineItemsList[2].properties.get(0).name.toString())
                                    .placeholder(R.drawable.ic_launcher_foreground)
                                    .error(R.drawable.ic_launcher_background)
                                    .into(profileBinding.profileSecondWishListItemImage)
                                profileBinding.profileSecondWishListItemPrice.text = formatDecimal(
                                    lineItemsList[2].price!!.toDouble() * MySharedPreferences
                                        .getInstance(requireContext()).getExchangeRate()
                                ) + " " + "${
                                    MySharedPreferences.getInstance(requireContext())
                                        .getCurrencyCode()
                                }"
                            }


                        }
                        is State.Failure -> {
                            profileBinding.favProgressBar.visibility = View.GONE
                            profileBinding.profileFirstWishListCardView.visibility = View.GONE
                            profileBinding.profileSecondWishListCardView.visibility = View.GONE
                            profileBinding.profileMoreOrdersTv.visibility = View.GONE
                            profileBinding.noFavouritesYet.text =
                                getString(R.string.no_network_connection)
                            profileBinding.noFavouritesYet.visibility = View.VISIBLE

                        }
                    }

                }
            }
        } else {
            profileBinding.favProgressBar.visibility = View.GONE
            profileBinding.profileFirstWishListCardView.visibility = View.GONE
            profileBinding.profileSecondWishListCardView.visibility = View.GONE
            profileBinding.profileMoreOrdersTv.visibility = View.GONE
            profileBinding.noFavouritesYet.text = getString(R.string.no_network_connection)
            profileBinding.noFavouritesYet.visibility = View.VISIBLE
        }


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
                MySharedPreferences.getInstance(requireContext()).saveISLogged(false)
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