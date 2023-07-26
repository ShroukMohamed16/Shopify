package com.example.shopify

import android.app.Application
import android.util.Log
import com.example.shopify.homeFragment.Model.Repository.BrandsRepository
import com.example.shopify.homeFragment.Remote.BrandsClient
import com.example.shopify.utilities.MyPriceRules
import com.example.shopify.utilities.checkConnectivity
import com.paypal.checkout.PayPalCheckout
import com.paypal.checkout.config.CheckoutConfig
import com.paypal.checkout.config.Environment
import com.paypal.checkout.config.SettingsConfig
import com.paypal.checkout.createorder.CurrencyCode
import com.paypal.checkout.createorder.UserAction
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class App : Application() {
    var YOUR_CLIENT_ID = "AR1RF8SwyYYNScjOhH6BjWL4wkpUApI_G6WbPPbj0Dw6w-qaU8CQno-srkC5BtVemRvRC9lBDnPnCEOH"
    var returnUrl = "nativexo://paypalpay"
    lateinit var repository: BrandsRepository
    override fun onCreate() {
        super.onCreate()
        if (checkConnectivity(this)) {
            repository = BrandsRepository.getInstance(BrandsClient())

            CoroutineScope(Dispatchers.IO).launch {
                repository.getPriceRules().collect {
                    for (i in 0 until it.price_rules.size) {
                        MyPriceRules.addPriceRule(it.price_rules[i].id, it.price_rules[i].title)
                        Log.i("TAG", "onCreate: app")
                    }
                }
            }

        }
        val config = CheckoutConfig(
            application = this,
            clientId = YOUR_CLIENT_ID,
            environment = Environment.SANDBOX,
            returnUrl = returnUrl,
            currencyCode = CurrencyCode.USD,
            userAction = UserAction.PAY_NOW,
            settingsConfig = SettingsConfig(
                loggingEnabled = true
            )
        )
        PayPalCheckout.setConfig(config)


    }
}