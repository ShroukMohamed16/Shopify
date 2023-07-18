package com.example.shopify.SubCategoryFragment.UI.View

import android.annotation.SuppressLint
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
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.example.shopify.R
import com.example.shopify.SubCategoryFragment.Model.AllProduct
import com.example.shopify.SubCategoryFragment.Model.Product
import com.example.shopify.SubCategoryFragment.Model.SubCategoryRepository.SubCategoryRepository
import com.example.shopify.SubCategoryFragment.Remote.ProductBrandClient
import com.example.shopify.SubCategoryFragment.UI.ViewModel.SubCategoryViewModel.SubCategoryViewModel
import com.example.shopify.SubCategoryFragment.UI.ViewModel.SubCategoryViewModelFactory.SubCategoryViewModelFactory
import com.example.shopify.base.State
import com.example.shopify.databinding.FragmentSubCategoryBinding
import com.example.shopify.homeFragment.Model.DataCalss.SmartCollection
import com.example.shopify.homeFragment.Model.Repository.BrandsRepository
import com.example.shopify.homeFragment.Remote.BrandsClient
import com.example.shopify.homeFragment.UI.View.FileName
import com.example.shopify.homeFragment.UI.ViewModel.HomeViewModel.HomeViewModel
import com.example.shopify.homeFragment.UI.ViewModel.HomeViewModelFactory.HomeViewModelFactory
import kotlinx.coroutines.launch

class SubCategoryFragment : Fragment(), OnClickProduct {
    lateinit var subCatBinding: FragmentSubCategoryBinding
    lateinit var productAdapter: ProductAdapter
    lateinit var factory: SubCategoryViewModelFactory
    lateinit var viewModel: SubCategoryViewModel
    private var productsList: List<Product> = listOf()
    lateinit var pref: SharedPreferences
    lateinit var editor: SharedPreferences.Editor

    private val args: SubCategoryFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        subCatBinding = FragmentSubCategoryBinding.inflate(inflater, container, false)
        return subCatBinding.root
    }

    @SuppressLint("ResourceAsColor")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pref = requireActivity().getSharedPreferences(FileName, Context.MODE_PRIVATE)
        editor = pref.edit()
        val destination = pref.getString("destination", "null")
        Log.i("TAG", "onViewCreated: $destination")

        var productType = "SHOES"

        factory =
            SubCategoryViewModelFactory(SubCategoryRepository.getInstance(ProductBrandClient()))
        viewModel = ViewModelProvider(this, factory).get(SubCategoryViewModel::class.java)

        val brand = args.brandName
        val categoryID = args.categoryID?.toLong()
        Log.i("TAG", "onViewCreated: $brand")
        Log.i("TAG", "onViewCreated: $categoryID")
        productAdapter = ProductAdapter(productsList, requireContext(), this)
        if (destination == "brand") {
            viewModel.getProductOfBrands(brand)

            lifecycleScope.launch {
                viewModel.productBrand.collect { result ->
                    when (result) {
                        is State.Loading -> {
                            subCatBinding.progressBar.visibility = View.VISIBLE
                            subCatBinding.productsRecyclerview.visibility = View.GONE


                        }
                        is State.Success -> {
                            productsList = result.data.products
                            if (productsList.isEmpty()) {
                                subCatBinding.productsRecyclerview.visibility = View.GONE
                                subCatBinding.noItemsfound.visibility = View.VISIBLE
                                subCatBinding.progressBar.visibility = View.GONE

                            } else {
                                subCatBinding.noItemsfound.visibility = View.GONE
                                subCatBinding.progressBar.visibility = View.GONE
                                subCatBinding.productsRecyclerview.visibility = View.VISIBLE

                                val gridLayoutmanager = GridLayoutManager(requireContext(), 2,
                                    GridLayoutManager.VERTICAL, false)
                                productAdapter.setProductList(productsList)
                                subCatBinding.productsRecyclerview.layoutManager = gridLayoutmanager
                                subCatBinding.productsRecyclerview.adapter = productAdapter
                            }

                        }
                        else -> {
                            Log.i("TAG", "onViewCreated: failur")


                        }
                    }


                }

            }
        } else if (destination == "category") {
            Log.i("TAG", "onViewCreated: enter the category scoop")

            if (categoryID != null){
                viewModel.getProductOfCategory( productType,categoryID)
                Log.i("TAG", "onViewCreated: the id is not nuull")
                subCatBinding.shoesTextView.setOnClickListener {
                    Log.i("TAG", "onViewCreated: choose shoes ")
                    productType = "SHOES"
                    resetButtonBackgrounds()
                    subCatBinding.shoesTextView.setBackgroundColor(R.color.gray)
                    viewModel.getProductOfCategory(productType, categoryID)
                }
                subCatBinding.tshirtTextView.setOnClickListener {
                    Log.i("TAG", "onViewCreated: choose teshirt ")
                    productType = "T-SHIRTS"
                    resetButtonBackgrounds()
                    subCatBinding.tshirtTextView.setBackgroundColor(R.color.gray)
                    viewModel.getProductOfCategory(productType, categoryID)
                }
                subCatBinding.accessoriesTextView.setOnClickListener {
                    Log.i("TAG", "onViewCreated: choose accessories ")
                    productType = "ACCESSORIES"
                    resetButtonBackgrounds()
                    subCatBinding.accessoriesTextView.setBackgroundColor(R.color.gray)
                    viewModel.getProductOfCategory( productType,categoryID)
                }



                lifecycleScope.launch{
                    viewModel.productCategory.collect{ result ->
                        when (result) {
                            is State.Loading -> {
                                Log.i("TAG", "onViewCreated: loaaaaaaaaading")
                                subCatBinding.progressBar.visibility = View.VISIBLE
                                subCatBinding.productsRecyclerview.visibility = View.GONE


                            }
                            is State.Success -> {
                                productsList = result.data.products
                                Log.i("TAG", "onViewCreated: $productsList")
                                if (productsList.isEmpty()) {
                                    subCatBinding.productsRecyclerview.visibility = View.GONE
                                    subCatBinding.noItemsfound.visibility = View.VISIBLE
                                    subCatBinding.progressBar.visibility = View.GONE

                                } else {
                                    subCatBinding.noItemsfound.visibility = View.GONE
                                    subCatBinding.progressBar.visibility = View.GONE
                                    subCatBinding.productsRecyclerview.visibility = View.VISIBLE

                                    val gridLayoutmanager = GridLayoutManager(requireContext(), 2,
                                        GridLayoutManager.VERTICAL, false)
                                    productAdapter.setProductList(productsList)
                                    subCatBinding.productsRecyclerview.layoutManager = gridLayoutmanager
                                    subCatBinding.productsRecyclerview.adapter = productAdapter
                                }

                            }
                            else -> {
                                Log.i("TAG", "onViewCreated: failur")


                            }
                        }


                    }
                }



            }


        }


    }
    private fun resetButtonBackgrounds() {
        subCatBinding.shoesTextView.setBackgroundResource(0)
        subCatBinding.tshirtTextView.setBackgroundResource(0)
        subCatBinding.accessoriesTextView.setBackgroundResource(0)
    }


    override fun onClickProduct(productID: Long) {
        //  TODO("Not yet implemented")
    }
}