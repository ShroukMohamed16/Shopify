package com.example.shopify.homeFragment.UI.View


import android.content.Context

import android.annotation.SuppressLint
import android.app.AlertDialog
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
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.shopify.R
import com.example.shopify.base.State
import com.example.shopify.databinding.CopounDialogBinding
import com.example.shopify.databinding.FragmentHomeBinding
import com.example.shopify.homeFragment.Model.DataCalss.SmartCollection
import com.example.shopify.homeFragment.Model.Repository.BrandsRepository
import com.example.shopify.homeFragment.Remote.BrandsClient
import com.example.shopify.homeFragment.UI.ViewModel.HomeViewModel.HomeViewModel
import com.example.shopify.homeFragment.UI.ViewModel.HomeViewModelFactory.HomeViewModelFactory
import com.example.shopify.network
import com.example.shopify.utilities.MyPriceRules
import com.example.shopify.utilities.MySharedPreferences
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.*
import java.lang.Runnable

const val FileName = "prefFile"

class HomeFragment : Fragment(), OnBrandClick, OnAdsClickListener {
    lateinit var viewModel: HomeViewModel
    lateinit var factory: HomeViewModelFactory
    lateinit var brandAdapter: BrandAdapter
    lateinit var gridLayoutmanager: GridLayoutManager
    lateinit var homeBinding: FragmentHomeBinding
    private var brandList: List<SmartCollection> = listOf()
    lateinit var pref: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    lateinit var adsAdapter: AdsAdapter
    lateinit var adsViewPager: ViewPager2
    private lateinit var handler: Handler
    private var currentItem = 0
    private var connectivityReceiver: BroadcastReceiver? = null
    private var copouns: MutableList<String> = mutableListOf()

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

        adsAdapter = AdsAdapter(listOf(R.drawable.ads_one,
            R.drawable.ads_three,
            R.drawable.ads_two,
            R.drawable.ads_four,
            R.drawable.ads_five), this)
        adsViewPager.adapter = adsAdapter

        adsViewPager.registerOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    currentItem = position
                }
            })

        handler = Handler(Looper.getMainLooper())
        handler.postDelayed(
            object : Runnable {
                override fun run() {
                    currentItem = (currentItem + 1) % adsAdapter.itemCount
                    adsViewPager.setCurrentItem(currentItem, true)
                    handler.postDelayed(this, 6000) // Change the delay time as needed
                }
            }, 6000) // Change the delay time as needed

        homeBinding.searchByProduct.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_searchFragment)

        }
        homeBinding.searchIcon.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_searchFragment)
        }


    }

    override fun onBrandClick(brandName: String) {
        pref.edit().putString("destination", "brand").apply()
        if (network.isNetworkAvailable(requireContext())) {
            val action =
                HomeFragmentDirections.actionHomeFragmentToSubCategoryFragment(brandName, "1")
            homeBinding.root.findNavController().navigate(action)
        } else {
            Snackbar.make(homeBinding.root,
                R.string.no_network_connection,
                Snackbar.LENGTH_LONG)
                .show()
        }
    }

    fun getCopouns() {
        lifecycleScope.launch {
            Log.i("MYTAG", "onCreate: one")
            viewModel.getAllDiscount(MyPriceRules.rules[0].id)
            viewModel.discount.collect { result ->
                when (result) {
                    is State.Loading -> {
                        Log.i("MYTAG", "onCreate: loaaaaaaaading")
                    }
                    is State.Success -> {
                        Log.i("MYTAG", "onCreate: successsssssss")
                        Log.i("MYTAG", "onCreate: ${result.data}")
                        copouns.add(result.data.discount_codes[0].code)
                        Log.i("MYTAG", "$copouns")
                    }
                    else -> {
                        Log.i("MYTAG", "onCreate: failur")
                    }
                }
            }
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
                    Snackbar.make(view!!,
                        R.string.no_network_connection,
                        Snackbar.LENGTH_LONG)
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


    fun displayCouponDialog(code: String) {
        val builder = AlertDialog.Builder(requireContext())
        val couponDialog = CopounDialogBinding.inflate(layoutInflater)
        builder.setView(couponDialog.root)
        val dialog = builder.create()
        dialog.show()
        couponDialog.CopounCodeTv.text = code
        couponDialog.copounSaveBtn.setOnClickListener {
            MySharedPreferences.getInstance(requireContext()).saveCouponCode(code)
            Toast.makeText(requireContext(), "coupon saved", Toast.LENGTH_SHORT)
            dialog.dismiss()
        }
        couponDialog.copounCancelBtn.setOnClickListener {
            dialog.dismiss()
        }
    }

    override fun onAdsClick(view: View, position: Int) {
        when (position) {
            0, 2, 4 -> {
                displayCouponDialog(MyPriceRules.rules[0].title)
            }
            else -> {
                displayCouponDialog(MyPriceRules.rules[1].title)
            }
        }
    }
}