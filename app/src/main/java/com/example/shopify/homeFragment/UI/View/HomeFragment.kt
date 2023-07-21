package com.example.shopify.homeFragment.UI.View


import android.content.Context

import android.annotation.SuppressLint
import android.content.*
import android.net.ConnectivityManager

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.shopify.R
import com.example.shopify.base.State
import com.example.shopify.databinding.FragmentHomeBinding
import com.example.shopify.homeFragment.Model.DataCalss.SmartCollection
import com.example.shopify.homeFragment.Model.Repository.BrandsRepository
import com.example.shopify.homeFragment.Remote.BrandsClient
import com.example.shopify.homeFragment.UI.ViewModel.HomeViewModel.HomeViewModel
import com.example.shopify.homeFragment.UI.ViewModel.HomeViewModelFactory.HomeViewModelFactory
import com.example.shopify.network
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
const val FileName = "prefFile"
class HomeFragment : Fragment(), OnBrandClick {
    lateinit var viewModel: HomeViewModel
    lateinit var factory: HomeViewModelFactory
    lateinit var brandAdapter: BrandAdapter
    lateinit var gridLayoutmanager: GridLayoutManager
    lateinit var homeBinding: FragmentHomeBinding
    private var brandList: List<SmartCollection> = listOf()
    lateinit var pref :SharedPreferences
    lateinit var editor :SharedPreferences.Editor
    lateinit var adsAdapter: AdsAdapter
    lateinit var adsViewPager : ViewPager2
    private lateinit var handler: Handler
    private var currentItem = 0
    private var connectivityReceiver: BroadcastReceiver?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        homeBinding = FragmentHomeBinding.inflate(inflater, container, false)
        return homeBinding.root
    }


    override fun onStart() {
        super.onStart()
        checkNetworkAtRuntime()

    }

    @SuppressLint("SuspiciousIndentation")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pref = requireActivity().getSharedPreferences(FileName, Context.MODE_PRIVATE)
        editor = pref.edit()
        factory = HomeViewModelFactory(BrandsRepository.getInstance(BrandsClient()))
        viewModel = ViewModelProvider(this, factory)[HomeViewModel::class.java]

        adsViewPager = homeBinding.adsViewPager

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


        viewModel.getAllDiscount()
        Log.i("MYTAG", "onCreate: get all Discount")
        lifecycleScope.launch {
            viewModel.discount.collect { result ->
                when (result) {
                    is State.Loading -> {
                        Log.i("MYTAG", "onCreate: loaaaaaaaading")


                    }
                    is State.Success -> {
                        Log.i("MYTAG", "onCreate: successsssssss")
                        Log.i("MYTAG", "onCreate: ${result.data}")

                    }
                    else -> {
                        Log.i("MYTAG", "onCreate: failur")

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

            adsAdapter = AdsAdapter(listOf(R.drawable.ads_one, R.drawable.ads_three, R.drawable.ads_two, R.drawable.ads_four, R.drawable.ads_five))
            adsViewPager.adapter = adsAdapter
            handler = Handler(Looper.getMainLooper())
            handler.postDelayed(
            object : Runnable {
                override fun run() {
                    currentItem = (currentItem + 1) % adsAdapter.itemCount
                    adsViewPager.setCurrentItem(currentItem, true)
                    handler.postDelayed(this, 3000) // Change the delay time as needed
                }
            }, 4000) // Change the delay time as needed

            adsViewPager.registerOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    currentItem = position
                }
            })

        }





        override fun onBrandClick(brandName: String) {
            pref.edit().putString("destination", "brand").apply()
            if(network.isNetworkAvailable(requireContext())){
            val action =
                HomeFragmentDirections.actionHomeFragmentToSubCategoryFragment(brandName, "1")
            homeBinding.root.findNavController().navigate(action)
            }else{
                Snackbar.make(homeBinding.root, R.string.no_network_connection, Snackbar.LENGTH_LONG)
                    .show()
            }
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

        private fun checkNetworkAtRuntime() {
            val connectivityManager =
                activity?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val intentFilter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)

            connectivityReceiver = object : BroadcastReceiver() {
                override fun onReceive(context: Context?, intent: Intent?) {
                    val networkInfo = connectivityManager.activeNetworkInfo

                    if (networkInfo == null || !networkInfo.isConnected) {
                        homeBinding.adsViewPager.visibility = View.GONE
                        homeBinding.searchEditText.visibility = View.GONE
                        homeBinding.txtSearch.visibility = View.GONE
                        homeBinding.brandTextView.visibility = View.GONE
                        homeBinding.brandsRV.visibility = View.GONE
                        homeBinding.noInternetText.visibility = View.VISIBLE
                        homeBinding.noInternetConnectionAni.visibility = View.VISIBLE
                        Snackbar.make(view!!, R.string.no_network_connection, Snackbar.LENGTH_LONG)
                            .show()
                    } else {
                        homeBinding.adsViewPager.visibility = View.VISIBLE
                        homeBinding.searchEditText.visibility = View.VISIBLE
                        homeBinding.txtSearch.visibility = View.VISIBLE
                        homeBinding.brandTextView.visibility = View.VISIBLE
                        homeBinding.brandsRV.visibility = View.VISIBLE
                        homeBinding.noInternetText.visibility = View.GONE
                        homeBinding.noInternetConnectionAni.visibility = View.GONE
                    }
                }
            }

            activity?.registerReceiver(connectivityReceiver, intentFilter)
        }


    }



