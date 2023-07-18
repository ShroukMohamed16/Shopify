package com.example.shopify.homeFragment.UI.View

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.shopify.R
import com.example.shopify.base.State
import com.example.shopify.databinding.FragmentHomeBinding
import com.example.shopify.homeFragment.Model.DataCalss.SmartCollection
import com.example.shopify.homeFragment.Model.Repository.BrandsRepository
import com.example.shopify.homeFragment.Remote.BrandsClient
import com.example.shopify.homeFragment.UI.ViewModel.HomeViewModel.HomeViewModel
import com.example.shopify.homeFragment.UI.ViewModel.HomeViewModelFactory.HomeViewModelFactory
import kotlinx.coroutines.launch

class HomeFragment : Fragment(), OnBrandClick {
    lateinit var viewModel: HomeViewModel
    lateinit var factory: HomeViewModelFactory
    lateinit var brandAdapter: BrandAdapter
    lateinit var gridLayoutmanager: GridLayoutManager
    lateinit var homeBinding: FragmentHomeBinding
    private var brandList: List<SmartCollection> = listOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        homeBinding = FragmentHomeBinding.inflate(inflater, container, false)
        return homeBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        factory = HomeViewModelFactory(BrandsRepository.getInstance(BrandsClient()))
        viewModel = ViewModelProvider(this, factory).get(HomeViewModel::class.java)


        viewModel.getAllBrands()
        brandAdapter = BrandAdapter(brandList, requireContext(), this)
        Log.i("TAG", "onViewCreated: get all brands")
        lifecycleScope.launch {
            viewModel.brand.collect { result ->
                when (result) {
                    is State.Loading -> {
                        homeBinding.progressBar.visibility = View.VISIBLE
                        homeBinding.brandsRV.visibility = View.GONE
                        homeBinding.brandTextView.visibility = View.GONE
                        Log.i("TAG", "onViewCreated: loaaaaaaaading")


                    }
                    is State.Success -> {
                        Log.i("TAG", "onViewCreated: successsssssss")
                        homeBinding.progressBar.visibility = View.GONE
                        homeBinding.brandsRV.visibility = View.VISIBLE
                        homeBinding.brandTextView.visibility = View.VISIBLE
                        brandList = result.data.smart_collections
                        gridLayoutmanager = GridLayoutManager(requireContext(), 2,
                            GridLayoutManager.VERTICAL, false)
                        brandAdapter.setBrandList(brandList)
                        homeBinding.brandsRV.layoutManager = gridLayoutmanager
                        homeBinding.brandsRV.adapter = brandAdapter


                    }
                    else -> {
                        Log.i("TAG", "onViewCreated: failur")


                    }
                }

            }
        }
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // This method is called before the text is changed.
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val filteredList = filteredMyListWithSequence(s.toString())
                showNoMatchingResultIfFilteredListIsEmpty(filteredList)
                if (filteredList != null) {
                    brandAdapter.setBrandList(filteredList)
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // This method is called after the text has been changed.
            }
        }

        homeBinding.searchEditText.addTextChangedListener(textWatcher)

    }


    override fun onBrandClick(brandName: String) {
        val action = HomeFragmentDirections.actionHomeFragmentToSubCategoryFragment(brandName)
        homeBinding.root.findNavController().navigate(action)
    }

    private fun filteredMyListWithSequence(s: String): List<SmartCollection>? {

        return brandList?.filter { it.title!!.lowercase().startsWith(s.lowercase()) }
    }

    private fun showNoMatchingResultIfFilteredListIsEmpty(filteredList: List<SmartCollection>?) {
        if (filteredList.isNullOrEmpty()) {
            homeBinding.txtNoResults.visibility = View.VISIBLE
            homeBinding.brandsRV.visibility = View.GONE
        } else {

            homeBinding.txtNoResults.visibility = View.GONE
            homeBinding.brandsRV.visibility = View.VISIBLE
        }
    }


}

