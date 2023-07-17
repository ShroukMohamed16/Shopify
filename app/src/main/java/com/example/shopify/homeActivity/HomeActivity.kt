package com.example.shopify.homeActivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.example.shopify.R
import com.example.shopify.databinding.ActivityHomeBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity() {
    lateinit var navController: NavController
    lateinit var bottomNavigationView: BottomNavigationView
    lateinit var bindingHA: ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingHA = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(bindingHA.root)

        bottomNavigationView = findViewById(R.id.NavBar)
        navController = Navigation.findNavController(this, R.id.nav_host)
        NavigationUI.setupWithNavController(bottomNavigationView, navController)
        bottomNavigationView.setOnClickListener {
            if (it.id == R.id.homeFragment) {
                navController.navigate(R.id.homeFragment)
            } else if (it.id == R.id.categoryFragment) {
                navController.navigate(R.id.categoryFragment)
            } else if (it.id == R.id.favouriteFragment) {
                navController.navigate(R.id.favouriteFragment)
            } else if (it.id == R.id.cartFragment) {
                navController.navigate(R.id.cartFragment)
            }else if (it.id == R.id.profileFragment) {
                navController.navigate(R.id.profileFragment)
            }
        }
    }
}