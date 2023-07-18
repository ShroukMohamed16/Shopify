package com.example.shopify.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatDelegate
import com.example.shopify.OnBoarding.OnBoardingActivity
import com.example.shopify.R
import com.example.shopify.homeActivity.HomeActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        GlobalScope.launch(Dispatchers.Main) {
            delay(2500)
                val intent = Intent(this@SplashScreen, OnBoardingActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
        }

        Handler().postDelayed({
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()

        }, 2000)

    }
}