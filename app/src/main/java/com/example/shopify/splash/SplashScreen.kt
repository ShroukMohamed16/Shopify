package com.example.shopify.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import com.example.shopify.OnBoarding.OnBoardingActivity
import com.example.shopify.R
import com.example.shopify.authentication.ui.view.AuthenticationActivity


import com.example.shopify.homeActivity.HomeActivity

import com.example.shopify.utilities.MySharedPreferences
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val TAG = "SplashScreen"

class SplashScreen : AppCompatActivity() {

    val auth = FirebaseAuth.getInstance()
    val isLogged = MySharedPreferences.getInstance(this).getISLogged()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        if (!MySharedPreferences.getInstance(this@SplashScreen).getOnBoardingState()) {
            GlobalScope.launch(Dispatchers.Main) {

                Log.i(TAG, "onCreate: ${auth.currentUser.toString()}")

                delay(4000)
                val intent = Intent(this@SplashScreen, OnBoardingActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)

            }

        } else {
            GlobalScope.launch(Dispatchers.Main) {
                delay(4000)
                if (!isLogged) {
                    val intent = Intent(this@SplashScreen, AuthenticationActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                } else {
                    val intent = Intent(this@SplashScreen, HomeActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                }
            }

        }

    }
}
