package com.example.shopify.favourite.ui.view

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.example.shopify.base.DraftOrderResponse
import com.example.shopify.base.State
import com.example.shopify.base.line_items
import com.example.shopify.databinding.FragmentFavouriteBinding
import com.example.shopify.favourite.model.repository.FavouriteRepository
import com.example.shopify.favourite.remote.FavouriteClient
import com.example.shopify.favourite.ui.viewmodel.FavouriteViewModel
import com.example.shopify.favourite.ui.viewmodel.FavouriteViewModelFactory
import com.example.shopify.utilities.MySharedPreferences
import com.example.shopify.utilities.checkConnectivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

private const val TAG = "FavouriteFragment"
class FavouriteFragment : Fragment(),OnClickListener{

    lateinit var binding: FragmentFavouriteBinding
    var favouriteAdapter = FavouriteAdapter(listOf(),this)
    lateinit var favouriteViewModel: FavouriteViewModel
    lateinit var favouriteViewModelFactory:FavouriteViewModelFactory
    lateinit var draftOrderResponse:DraftOrderResponse

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

        if(checkConnectivity(requireContext())){
        favouriteViewModel.getFavDraftOrder(MySharedPreferences.getInstance(requireContext()).getFavID()!!)

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
                        if(result.data.draft_order!!.line_items.size > 1){

                            val lineItemsList = result.data.draft_order!!.line_items
                            binding.noFavTxt.visibility = View.GONE
                            binding.favProgressBar.visibility = View.GONE
                            draftOrderResponse = result.data
                            binding.favRv.visibility = View.VISIBLE
                            favouriteAdapter.setFavList(lineItemsList)
                            binding.favRv.adapter = favouriteAdapter
                        }else{
                            binding.favProgressBar.visibility = View.GONE
                            binding.noFavTxt.visibility = View.VISIBLE
                            binding.favRv.visibility = View.GONE

                        }


                    }
                    is State.Failure ->{
                        Toast.makeText(context,"Fail to get draft order",Toast.LENGTH_LONG).show()

                    }

                }

            }
        }
        }else{
            binding.favRv.visibility = View.GONE
            binding.noFavTxt.visibility = View.GONE
            binding.favProgressBar.visibility = View.GONE
            Snackbar.make(view,"No Internet Connection",Snackbar.LENGTH_LONG)
                .show()

        }

    }

    override fun onClickDeleteIcon(lineItem: line_items) {
        favouriteAdapter.setFavList(listOf())
        val list = draftOrderResponse.draft_order?.line_items
        val mutableList = list?.toMutableList()
        mutableList?.remove(lineItem)
        favouriteAdapter.setFavList(mutableList)
        draftOrderResponse.draft_order?.line_items = mutableList!!.toList()
        favouriteViewModel.modifyFavDraftOrder(
            MySharedPreferences.getInstance(
                requireContext()
            ).getFavID()!!, draftOrderResponse
        )


    }

    override fun onClickProductCard(id: Long) {
        val action =
            FavouriteFragmentDirections.actionFavouriteFragmentToProductInfoFragment2(id.toString())
        binding.root.findNavController().navigate(action)
    }


}
