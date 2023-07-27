package com.example.shopify.favourite.ui.view

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.example.shopify.R
import com.example.shopify.base.DraftOrderResponse
import com.example.shopify.base.State
import com.example.shopify.base.line_items
import com.example.shopify.databinding.FragmentFavouriteBinding
import com.example.shopify.favourite.model.repository.FavouriteRepository
import com.example.shopify.favourite.remote.FavouriteClient
import com.example.shopify.favourite.ui.viewmodel.FavouriteViewModel
import com.example.shopify.favourite.ui.viewmodel.FavouriteViewModelFactory
import com.example.shopify.productinfo.model.pojo.toLineItems
import com.example.shopify.search.ui.view.SearchFragmentDirections
import com.example.shopify.utilities.MySharedPreferences
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class FavouriteFragment : Fragment(), OnClickListener {

    lateinit var binding: FragmentFavouriteBinding
    lateinit var favouriteAdapter: FavouriteAdapter
    lateinit var favouriteViewModel: FavouriteViewModel
    lateinit var favouriteViewModelFactory: FavouriteViewModelFactory
    private var favDraftOrderResponse : DraftOrderResponse = DraftOrderResponse()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentFavouriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        favouriteViewModelFactory =
            FavouriteViewModelFactory(FavouriteRepository.getInstance(FavouriteClient()))
        favouriteViewModel =
            ViewModelProvider(this, favouriteViewModelFactory)[FavouriteViewModel::class.java]
        favouriteAdapter = FavouriteAdapter(listOf(), this)

        favouriteViewModel.getFavDraftOrder(MySharedPreferences.getInstance(requireContext())
            .getFavID().toString())

        lifecycleScope.launch {
            favouriteViewModel.draftOrderDetailsList.collect { result ->
                when (result) {
                    is State.Loading -> {
                        binding.favProgressBar.visibility = View.VISIBLE
                        binding.favRv.visibility = View.GONE
                        binding.noFavTxt.visibility = View.GONE
                        Toast.makeText(context, "Loading", Toast.LENGTH_LONG).show()

                    }
                    is State.Success -> {
                        val lineItemsList = result.data.draft_order!!.line_items
                        if (lineItemsList.isNullOrEmpty()) {
                            binding.noFavTxt.visibility = View.VISIBLE
                            binding.favProgressBar.visibility = View.GONE
                            binding.favRv.visibility = View.GONE


                        } else {
                            binding.noFavTxt.visibility = View.GONE
                            binding.favProgressBar.visibility = View.GONE
                            binding.favRv.visibility = View.VISIBLE
                            favouriteAdapter.setFavList(lineItemsList)
                            binding.favRv.adapter = favouriteAdapter

                        }
                    }
                    is State.Failure -> {
                        Toast.makeText(context, "Fail to get draft order", Toast.LENGTH_LONG).show()

                    }

                }

            }
        }

    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onClickDeleteIcon(lineItems: line_items) {
        favouriteViewModel.getFavDraftOrder(MySharedPreferences.getInstance(requireContext())
            .getFavID().toString())
        lifecycleScope.launch {
            favouriteViewModel.draftOrderDetailsList.collect { result ->
                when (result) {
                    is State.Loading -> {
                        Toast.makeText(context, "Loading", Toast.LENGTH_LONG).show()
                        Toast.makeText(context, "Loading", Toast.LENGTH_LONG).show()

                    }
                    is State.Success -> {
                        val alertDialog = AlertDialog.Builder(context)

                        alertDialog.apply {
                            setIcon(R.drawable.delete_icon)
                            setTitle("Delete")
                            setMessage("Are you sure you want to delete the Product from favorite?")
                            setPositiveButton("Yes") { _: DialogInterface?, _: Int ->
                                favDraftOrderResponse = result.data
                                favDraftOrderResponse.draft_order?.line_items?.remove(lineItems)
                                favouriteViewModel.modifyFavDraftOrder(MySharedPreferences.getInstance(requireContext()).getFavID().toString(),
                                    favDraftOrderResponse)
                            }
                        }.create().show()
                    }
                    is State.Failure -> {
                        Toast.makeText(requireContext(),
                            "Fail to get Draft Order",
                            Toast.LENGTH_LONG)
                            .show()

                    }

                }

            }
        }

    }

    override fun onClickProductCard(id: Long) {
        val action =
            FavouriteFragmentDirections.actionFavouriteFragmentToProductInfoFragment2(id.toString())
        binding.root.findNavController().navigate(action)
    }

}
