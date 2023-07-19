package com.example.shopify.SubCategoryFragment.UI.View

import android.annotation.SuppressLint
import android.content.*
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.example.shopify.R
import com.example.shopify.SubCategoryFragment.Model.Product
import com.example.shopify.SubCategoryFragment.Model.SubCategoryRepository.SubCategoryRepository
import com.example.shopify.SubCategoryFragment.Remote.ProductBrandClient
import com.example.shopify.SubCategoryFragment.UI.ViewModel.SubCategoryViewModel.SubCategoryViewModel
import com.example.shopify.SubCategoryFragment.UI.ViewModel.SubCategoryViewModelFactory.SubCategoryViewModelFactory
import com.example.shopify.base.State
import com.example.shopify.databinding.FragmentSubCategoryBinding
import com.example.shopify.homeFragment.UI.View.FileName
import com.example.shopify.network
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class SubCategoryFragment : Fragment(), OnClickProduct {
    lateinit var subCatBinding: FragmentSubCategoryBinding
    lateinit var productAdapter: ProductAdapter
    lateinit var factory: SubCategoryViewModelFactory
    lateinit var viewModel: SubCategoryViewModel
    private var productsList: List<Product> = listOf()
    lateinit var pref: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    private var clicked: Boolean = false
    lateinit var gridLayoutmanager: GridLayoutManager
    private var selectedFilter = "All Prices"
    private val args: SubCategoryFragmentArgs by navArgs()

    private var connectivityReceiver: BroadcastReceiver?=null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        subCatBinding = FragmentSubCategoryBinding.inflate(inflater, container, false)
        return subCatBinding.root
    }

    override fun onStart() {
        super.onStart()
        checkNetworkAtRuntime()

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
                                subCatBinding.filterList.onItemSelectedListener = object :
                                    AdapterView.OnItemSelectedListener {
                                    override fun onItemSelected(
                                        parent: AdapterView<*>,
                                        view: View?,
                                        position: Int,
                                        id: Long,
                                    ) {
                                        selectedFilter =
                                            parent.getItemAtPosition(position) as String
                                        val filtershoesList = when (selectedFilter) {
                                            "Lower that 100" -> productsList.filter { it.variants[0].price?.toDouble()!! < 100.00 }
                                            "More than 100" -> productsList.filter { it.variants[0].price?.toDouble()!! >= 100.00 }
                                            else -> productsList
                                        }
                                        productAdapter.setProductList(filtershoesList)
                                        Log.i("TAG", "onItemSelected: $productsList")
                                        Log.i("TAG", "onItemSelected: $selectedFilter")
                                    }

                                    override fun onNothingSelected(parent: AdapterView<*>) {
                                    }
                                }

                                gridLayoutmanager = GridLayoutManager(requireContext(), 2,
                                    GridLayoutManager.VERTICAL, false)
                                productAdapter.setProductList(productsList)
                                subCatBinding.productsRecyclerview.layoutManager = gridLayoutmanager
                                subCatBinding.productsRecyclerview.adapter = productAdapter


                                subCatBinding.allFAB.setOnClickListener {
                                    onClicked()

                                }
                                subCatBinding.shoesFAB.setOnClickListener {
                                    var shoesList =
                                        productsList.filter { it.product_type == "SHOES" }
                                    subCatBinding.filterList.onItemSelectedListener = object :
                                        AdapterView.OnItemSelectedListener {
                                        override fun onItemSelected(
                                            parent: AdapterView<*>,
                                            view: View?,
                                            position: Int,
                                            id: Long,
                                        ) {
                                            selectedFilter =
                                                parent.getItemAtPosition(position) as String
                                            val filtershoesList = when (selectedFilter) {
                                                "Lower that 100" -> shoesList.filter { it.variants[0].price?.toDouble()!! < 100.00 }
                                                "More than 100" -> shoesList.filter { it.variants[0].price?.toDouble()!! >= 100.00 }
                                                else -> shoesList
                                            }
                                            productAdapter.setProductList(filtershoesList)
                                            Log.i("TAG", "onItemSelected: $shoesList")
                                            Log.i("TAG", "onItemSelected: $selectedFilter")
                                        }

                                        override fun onNothingSelected(parent: AdapterView<*>) {
                                        }
                                    }

                                    gridLayoutmanager = GridLayoutManager(requireContext(), 2,
                                        GridLayoutManager.VERTICAL, false)
                                    productAdapter.setProductList(shoesList)
                                    subCatBinding.productsRecyclerview.layoutManager =
                                        gridLayoutmanager
                                    subCatBinding.productsRecyclerview.adapter = productAdapter


                                }


                                subCatBinding.shirtFAB.setOnClickListener {

                                    val tShirtList =
                                        productsList.filter { it.product_type == "T-SHIRTS" }
                                    subCatBinding.filterList.onItemSelectedListener = object :
                                        AdapterView.OnItemSelectedListener {
                                        override fun onItemSelected(
                                            parent: AdapterView<*>,
                                            view: View?,
                                            position: Int,
                                            id: Long,
                                        ) {
                                            selectedFilter =
                                                parent.getItemAtPosition(position) as String
                                            val filtershoesList = when (selectedFilter) {
                                                "Lower that 100" -> tShirtList.filter { it.variants[0].price?.toDouble()!! < 100.00 }
                                                "More than 100" -> tShirtList.filter { it.variants[0].price?.toDouble()!! >= 100.00 }
                                                else -> tShirtList
                                            }
                                            productAdapter.setProductList(filtershoesList)
                                            Log.i("TAG", "onItemSelected: $tShirtList")
                                            Log.i("TAG", "onItemSelected: $selectedFilter")
                                        }

                                        override fun onNothingSelected(parent: AdapterView<*>) {
                                        }
                                    }
                                    gridLayoutmanager = GridLayoutManager(requireContext(), 2,
                                        GridLayoutManager.VERTICAL, false)
                                    productAdapter.setProductList(tShirtList)
                                    subCatBinding.productsRecyclerview.layoutManager =
                                        gridLayoutmanager
                                    subCatBinding.productsRecyclerview.adapter = productAdapter

                                }
                                subCatBinding.accessoriesFAB.setOnClickListener {
                                    val accessList =
                                        productsList.filter { it.product_type == "ACCESSORIES" }
                                    subCatBinding.filterList.onItemSelectedListener = object :
                                        AdapterView.OnItemSelectedListener {
                                        override fun onItemSelected(
                                            parent: AdapterView<*>,
                                            view: View?,
                                            position: Int,
                                            id: Long,
                                        ) {
                                            selectedFilter =
                                                parent.getItemAtPosition(position) as String
                                            val filtershoesList = when (selectedFilter) {
                                                "Lower that 100" -> accessList.filter { it.variants[0].price?.toDouble()!! < 100.00 }
                                                "More than 100" -> accessList.filter { it.variants[0].price?.toDouble()!! >= 100.00 }
                                                else -> accessList
                                            }
                                            productAdapter.setProductList(filtershoesList)
                                            Log.i("TAG", "onItemSelected: $accessList")
                                            Log.i("TAG", "onItemSelected: $selectedFilter")
                                        }

                                        override fun onNothingSelected(parent: AdapterView<*>) {
                                        }
                                    }
                                    gridLayoutmanager = GridLayoutManager(requireContext(), 2,
                                        GridLayoutManager.VERTICAL, false)
                                    productAdapter.setProductList(accessList)
                                    subCatBinding.productsRecyclerview.layoutManager =
                                        gridLayoutmanager
                                    subCatBinding.productsRecyclerview.adapter = productAdapter

                                }


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

            if (categoryID != null) {
                viewModel.getProductOfCategory(productType, categoryID)
                Log.i("TAG", "onViewCreated: the id is not nuull")

                subCatBinding.allFAB.setOnClickListener {
                    viewModel.getAllProductOfCategory(categoryID)
                    onClicked()

                }
                subCatBinding.shoesFAB.setOnClickListener {

                    Log.i("TAG", "onViewCreated: choose shoes ")
                    productType = "SHOES"
                    viewModel.getProductOfCategory(productType, categoryID)
                }


                subCatBinding.shirtFAB.setOnClickListener {
                    Log.i("TAG", "onViewCreated: choose teshirt ")
                    productType = "T-SHIRTS"
                    viewModel.getProductOfCategory(productType, categoryID)
                }
                subCatBinding.accessoriesFAB.setOnClickListener {
                    Log.i("TAG", "onViewCreated: choose accessories ")
                    productType = "ACCESSORIES"
                    viewModel.getProductOfCategory(productType, categoryID)
                }



                lifecycleScope.launch {
                    viewModel.productCategory.collect { result ->
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

                                    subCatBinding.filterList.onItemSelectedListener = object :
                                        AdapterView.OnItemSelectedListener {
                                        override fun onItemSelected(
                                            parent: AdapterView<*>,
                                            view: View?,
                                            position: Int,
                                            id: Long,
                                        ) {
                                            selectedFilter =
                                                parent.getItemAtPosition(position) as String
                                            val filtershoesList = when (selectedFilter) {
                                                "Lower that 100" -> productsList.filter { it.variants[0].price?.toDouble()!! < 100.00 }
                                                "More than 100" -> productsList.filter { it.variants[0].price?.toDouble()!! >= 100.00 }
                                                else -> productsList
                                            }
                                            productAdapter.setProductList(filtershoesList)
                                            Log.i("TAG", "onItemSelected: $productsList")
                                            Log.i("TAG", "onItemSelected: $selectedFilter")
                                        }

                                        override fun onNothingSelected(parent: AdapterView<*>) {
                                        }
                                    }
                                    val gridLayoutmanager = GridLayoutManager(requireContext(), 2,
                                        GridLayoutManager.VERTICAL, false)
                                    productAdapter.setProductList(productsList)
                                    subCatBinding.productsRecyclerview.layoutManager =
                                        gridLayoutmanager
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

    override fun onStop() {
        super.onStop()
        activity?.unregisterReceiver(connectivityReceiver)
    }

    override fun onClickProduct(productID: Long) {
        //  TODO("Not yet implemented")
    }

    private fun onClicked() {
        setVisibility(clicked)
        setAnimation(clicked)
        clicked = !clicked
    }

    private fun setVisibility(clicked: Boolean) {
        if (!clicked) {
            subCatBinding.shoesFAB.setVisibility(View.VISIBLE)
            subCatBinding.shirtFAB.setVisibility(View.VISIBLE)
            subCatBinding.accessoriesFAB.setVisibility(View.VISIBLE)
        } else {
            subCatBinding.shoesFAB.setVisibility(View.INVISIBLE)
            subCatBinding.shirtFAB.setVisibility(View.INVISIBLE)
            subCatBinding.accessoriesFAB.setVisibility(View.INVISIBLE)
        }
    }

    private fun setAnimation(clicked: Boolean) {
        if (!clicked) {
            subCatBinding.shoesFAB.startAnimation(AnimationUtils.loadAnimation(context,
                R.anim.from_bottom))
            subCatBinding.shirtFAB.startAnimation(AnimationUtils.loadAnimation(context,
                R.anim.from_bottom))
            subCatBinding.accessoriesFAB.startAnimation(AnimationUtils.loadAnimation(context,
                R.anim.from_bottom))
            subCatBinding.allFAB.startAnimation(AnimationUtils.loadAnimation(context,
                R.anim.rotate_open))
        } else {
            subCatBinding.shoesFAB.startAnimation(AnimationUtils.loadAnimation(context,
                R.anim.to_bottom))
            subCatBinding.shirtFAB.startAnimation(AnimationUtils.loadAnimation(context,
                R.anim.to_bottom))
            subCatBinding.accessoriesFAB.startAnimation(AnimationUtils.loadAnimation(context,
                R.anim.to_bottom))
            subCatBinding.allFAB.startAnimation(AnimationUtils.loadAnimation(context,
                R.anim.rotate_close))
        }
    }

    private fun checkNetworkAtRuntime()
    {
        val connectivityManager = activity?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val intentFilter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)

        connectivityReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val networkInfo = connectivityManager.activeNetworkInfo

                if (networkInfo == null || !networkInfo.isConnected) {
                    Snackbar.make(view!!, R.string.no_network_connection, Snackbar.LENGTH_LONG).show()
                }
            }
        }

        activity?.registerReceiver(connectivityReceiver, intentFilter)
        }
}