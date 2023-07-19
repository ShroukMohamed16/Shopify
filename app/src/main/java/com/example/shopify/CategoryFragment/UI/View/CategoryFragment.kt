package com.example.shopify.CategoryFragment.UI.View

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shopify.CategoryFragment.Model.DataClass.CustomCollections
import com.example.shopify.CategoryFragment.Model.Repository.AllCategoriesRepository
import com.example.shopify.CategoryFragment.Remote.AllCategoriesClient
import com.example.shopify.CategoryFragment.UI.ViewModel.CategoryViewModel.AllCategoriesViewModel
import com.example.shopify.CategoryFragment.UI.ViewModel.CategoryViewModelFactory.AllCategoriesViewModelFactory
import com.example.shopify.R
import com.example.shopify.base.State
import com.example.shopify.databinding.FragmentCategoryBinding
import com.example.shopify.homeFragment.Model.Repository.BrandsRepository
import com.example.shopify.homeFragment.Remote.BrandsClient
import com.example.shopify.homeFragment.UI.View.FileName
import com.example.shopify.homeFragment.UI.ViewModel.HomeViewModel.HomeViewModel
import com.example.shopify.homeFragment.UI.ViewModel.HomeViewModelFactory.HomeViewModelFactory
import kotlinx.coroutines.launch


class CategoryFragment : Fragment(), OnClickCategory {
    lateinit var catBinding: FragmentCategoryBinding
    lateinit var factory: AllCategoriesViewModelFactory
    lateinit var viewModel: AllCategoriesViewModel
    lateinit var catAdapter: AllCategoriesAdapter
    lateinit var linearLayoutManager: LinearLayoutManager
    private var categoryList: List<CustomCollections> = listOf()
    lateinit var pref : SharedPreferences
    lateinit var editor : SharedPreferences.Editor
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        catBinding = FragmentCategoryBinding.inflate(inflater, container, false)
        return catBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pref = requireActivity().getSharedPreferences(FileName, Context.MODE_PRIVATE)
        editor = pref.edit()
        factory =
            AllCategoriesViewModelFactory(AllCategoriesRepository.getInstance(AllCategoriesClient()))
        viewModel = ViewModelProvider(this, factory).get(AllCategoriesViewModel::class.java)

        viewModel.getAllCategories()
        catAdapter = AllCategoriesAdapter(categoryList, requireContext(), this)
        lifecycleScope.launch {
            viewModel.category.collect { result ->
                when (result) {
                    is State.Loading -> {
                        Log.i("TAG", "onViewCreated: categ loaaaading")
                        catBinding.categoryProgressBar.visibility = View.VISIBLE
                        catBinding.categoryRecyclerView.visibility = View.GONE


                    }
                    is State.Success -> {
                        Log.i("TAG", "onViewCreated: categ succcesssss")
                        Log.i("TAG", "onViewCreated: categ ${result.data}")
                        catBinding.categoryProgressBar.visibility = View.GONE
                        catBinding.categoryRecyclerView.visibility = View.VISIBLE
                        categoryList = result.data.custom_collections
                        linearLayoutManager = LinearLayoutManager(requireContext())
                        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
                        catAdapter.setCategoryList(categoryList)
                        catBinding.categoryRecyclerView.layoutManager = linearLayoutManager
                        catBinding.categoryRecyclerView.adapter = catAdapter


                    }
                    else -> {
                        Log.i("TAG", "onViewCreated: failur")


                    }
                }

            }

        }
    }

    override fun onCategoryClick(categoryID: Long) {
        pref.edit().putString("destination" , "category").apply()
        val action = CategoryFragmentDirections.actionCategoryFragmentToSubCategoryFragment("",categoryID.toString())
        catBinding.root.findNavController().navigate(action)
    }


}




