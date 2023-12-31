package com.example.shopify.authentication.ui.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.example.shopify.R
import com.example.shopify.databinding.ActivityAuthenticationBinding

class AuthenticationActivity : AppCompatActivity() {

    lateinit var authenticationBinding: ActivityAuthenticationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        authenticationBinding = ActivityAuthenticationBinding.inflate(LayoutInflater.from(this))
        setContentView(authenticationBinding.root)

        val navController: NavController = Navigation.findNavController(this , R.id.auth_nav_host_fragment)
        NavigationUI.setupActionBarWithNavController(this,navController)

    }
}