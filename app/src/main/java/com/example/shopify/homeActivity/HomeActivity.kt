package com.example.shopify.homeActivity

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.example.shopify.R
import com.example.shopify.databinding.ActivityHomeBinding
import com.example.shopify.utilities.MySharedPreferences
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity() {
    lateinit var navController: NavController
    lateinit var bottomNavigationView: BottomNavigationView
    lateinit var bindingHA: ActivityHomeBinding
    val isGuest = MySharedPreferences.getInstance(this).getISGuest()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingHA = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(bindingHA.root)

        bottomNavigationView = findViewById(R.id.NavBar)
        navController = Navigation.findNavController(this, R.id.nav_host)

        if (isGuest) {
            val menu = bottomNavigationView.menu
            val menuItem = menu.findItem(R.id.favouriteFragment)
            val menuItem2 = menu.findItem(R.id.cartFragment)

            menuItem.isVisible = false
            menuItem2.isVisible = false




        }
        NavigationUI.setupWithNavController(bottomNavigationView, navController)

    }


}