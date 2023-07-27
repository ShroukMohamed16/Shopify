package com.example.shopify.favourite.ui.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.example.shopify.base.State
import com.example.shopify.base.line_items
import com.example.shopify.databinding.FragmentFavouriteBinding
import com.example.shopify.favourite.model.repository.FavouriteRepository
import com.example.shopify.favourite.remote.FavouriteClient
import com.example.shopify.favourite.ui.viewmodel.FavouriteViewModel
import com.example.shopify.favourite.ui.viewmodel.FavouriteViewModelFactory
import com.example.shopify.search.ui.view.SearchFragmentDirections
import com.example.shopify.utilities.MySharedPreferences
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class FavouriteFragment : Fragment(),OnClickListener{

    lateinit var binding: FragmentFavouriteBinding
    lateinit var favouriteAdapter: FavouriteAdapter
    lateinit var favouriteViewModel: FavouriteViewModel
    lateinit var favouriteViewModelFactory:FavouriteViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View{
        // Inflate the layout for this fragment
        binding =  FragmentFavouriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        favouriteViewModelFactory = FavouriteViewModelFactory(FavouriteRepository.getInstance(FavouriteClient()))
        favouriteViewModel = ViewModelProvider(this,favouriteViewModelFactory)[FavouriteViewModel::class.java]
        favouriteAdapter = FavouriteAdapter(listOf(),this)

        favouriteViewModel.getFavDraftOrder(MySharedPreferences.getInstance(requireContext()).getFavID().toString())

        lifecycleScope.launch{
            favouriteViewModel.draftOrderDetailsList.collect { result->
                when(result){
                    is State.Loading ->{
                        binding.favProgressBar.visibility = View.VISIBLE
                        binding.favRv.visibility = View.GONE
                        binding.noFavTxt.visibility = View.GONE
                        Toast.makeText(context,"Loading",Toast.LENGTH_LONG).show()

                    }
                    is State.Success ->{
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
                    is State.Failure ->{
                        Toast.makeText(context,"Fail to get draft order",Toast.LENGTH_LONG).show()

                    }

                }

            }
        }

    }

    override fun onClickDeleteIcon(lineItems: line_items) {
        favouriteViewModel.getFavDraftOrder(MySharedPreferences.getInstance(requireContext()).getFavID().toString())
        lifecycleScope.launch {
            favouriteViewModel.draftOrderDetails.collect{ result->
                when(result){
                    is State.Loading->{
                        Toast.makeText(context,"Loading",Toast.LENGTH_LONG).show()
                    }
                    is State.Success->{
                        var line_items_list = mutableListOf<line_items>()
                        line_items_list = result.data.draft_order!!.line_items.toMutableList()
                        line_items_list.remove(lineItems)
                        result.data.draft_order!!.line_items = line_items_list.toList()
                        var draft_order = result.data
                        favouriteViewModel.modifyFavDraftOrder(MySharedPreferences.getInstance(requireContext()).getFavID().toString()
                            ,draft_order)
                    }
                    is State.Failure ->{
                        Toast.makeText(requireContext(), "Fail to get Draft Order", Toast.LENGTH_LONG)
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
