package com.example.shopify

import android.app.Application
import android.util.Log
import com.example.shopify.homeFragment.Model.Repository.BrandsRepository
import com.example.shopify.homeFragment.Remote.BrandsClient
import com.example.shopify.utilities.MyPriceRules
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class App : Application() {

    lateinit var repository: BrandsRepository
    override fun onCreate() {
        super.onCreate()
        repository= BrandsRepository.getInstance(BrandsClient())

        CoroutineScope(Dispatchers.IO).launch{
            repository.getPriceRules().collect{
                for (i in 0 until it.price_rules.size){
                    MyPriceRules.addPriceRule(it.price_rules[i].id,it.price_rules[i].title)
                    Log.i("TAG", "onCreate: app")
                }
            }
        }

    }
}