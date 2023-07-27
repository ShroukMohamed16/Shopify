package com.example.shopify.search.ui.view

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.example.shopify.databinding.FragmentSearchBinding
import com.example.shopify.R
import com.example.shopify.SubCategoryFragment.Model.Product
import com.example.shopify.base.State
import com.example.shopify.homeFragment.Model.DataCalss.SmartCollection
import com.example.shopify.homeFragment.UI.View.HomeFragmentDirections
import com.example.shopify.network
import com.example.shopify.search.model.repository.SearchRepository
import com.example.shopify.search.remote.SearchClient
import com.example.shopify.search.ui.viewmodel.SearchViewModel
import com.example.shopify.search.ui.viewmodel.SearchViewModelFactory
import kotlinx.coroutines.launch


class SearchFragment : Fragment(), OnSearchItemClick {

    lateinit var binding: FragmentSearchBinding
    lateinit var searchViewModel: SearchViewModel
    lateinit var searchViewModelFactory: SearchViewModelFactory
    lateinit var searchAdapter: SearchAdapter
    var productList: List<Product> = listOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchViewModelFactory =
            SearchViewModelFactory(SearchRepository.getInstance(SearchClient()))
        searchViewModel =
            ViewModelProvider(this, searchViewModelFactory)[SearchViewModel::class.java]
        searchAdapter = SearchAdapter(productList, requireContext(), this)

        searchViewModel.getProducts()

        lifecycleScope.launch {
            searchViewModel.products.collect { result ->
                when (result) {
                    is State.Success -> {
                        binding.imageView.visibility = View.GONE
                        productList = result.data.getProducts()
                        searchAdapter.setProductList(productList)
                        binding.searchRv.adapter = searchAdapter

                    }
                    else -> {
                        Toast.makeText(requireContext(), "Fail to get Products", Toast.LENGTH_LONG)
                            .show()

                    }
                }


            }
        }


        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // This method is called before the text is changed.
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val filteredList = filteredProductList(s.toString())
                if (filteredList != null) {
                    searchAdapter.setProductList(filteredList)
                    binding.searchRv.adapter = searchAdapter
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // This method is called after the text has been changed.
            }
        }

        binding.searchViewEditText.addTextChangedListener(textWatcher)

    }

    override fun onProductClick(id: Long) {
        val action =
            SearchFragmentDirections.actionSearchFragmentToProductInfoFragment2(id.toString())
        binding.root.findNavController().navigate(action)
    }

    private fun filteredProductList(s: String): List<Product>? {
        return productList?.filter { it.title!!.lowercase().startsWith(s.lowercase())}
        }



}
