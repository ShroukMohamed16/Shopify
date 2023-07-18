package com.example.shopify.OnBoarding

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.shopify.R
import com.example.shopify.homeActivity.HomeActivity

class OnBoardingActivity : AppCompatActivity() {
    lateinit var onBoardingItemsAdapter: OnBoardingItemsAdapter
    lateinit var indicatorContainer:LinearLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_on_boarding)
        setOnboardingItems()
        setUpIndicators()
        setCurrentIndicator(0)
    }
    
    private fun setOnboardingItems() {
        val list = listOf(
            OnBoardingItem(
                R.drawable.onboarding_one,
                /*"Browse",*/
                "Discover the latest fashion trends with our app"
            ),
            OnBoardingItem(
                R.drawable.onboarding_one,
                /*"Search",*/
                "Easily search for the items you want with our search feature"
            ),
            OnBoardingItem(
                R.drawable.onboarding_one,
                /*"Cart",*/
                "Add items to your cart with just one click"
            ),
            OnBoardingItem(
                R.drawable.onboarding_one,
                /*"Favourite",*/
                "Save your favorite items for later with our Favorites feature"
            ),
            OnBoardingItem(
                R.drawable.onboarding_one,
                /*"Easy Payment",*/
                "Pay securely and conveniently with our online payment options or cash"
            ),
            OnBoardingItem(
                R.drawable.onboarding_one,
                /*"Delivery",*/
                "We deliver your order right to your doorstep"
            ),
            OnBoardingItem(
                R.drawable.onboarding_one,
                /*"Location",*/
                "Let us know where to deliver your order with our location form"
            ),

            )
        onBoardingItemsAdapter= OnBoardingItemsAdapter(list)
        val onBoardingViewPager = findViewById<ViewPager2>(R.id.onBoardingViewPager)
        onBoardingViewPager.adapter=onBoardingItemsAdapter
        onBoardingViewPager.registerOnPageChangeCallback(object:ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                setCurrentIndicator(position)
            }
        })
        (onBoardingViewPager.getChildAt(0) as RecyclerView).overScrollMode=RecyclerView.OVER_SCROLL_NEVER

        findViewById<ImageView>(R.id.onBoarding_next_image).setOnClickListener {
            if(onBoardingViewPager.currentItem +1<onBoardingItemsAdapter.itemCount){
                onBoardingViewPager.currentItem+=1
            }else{
                navigate()
            }
        }

        findViewById<Button>(R.id.onBoarding_getStarted_Btn).setOnClickListener {
            navigate()
        }
    }
    private fun navigate(){
        startActivity(Intent(applicationContext,HomeActivity::class.java))
    }
    private fun setUpIndicators(){
        indicatorContainer = findViewById(R.id.indicators_container)
        val indicators = arrayOfNulls<ImageView>(onBoardingItemsAdapter.itemCount)
        val layoutParams:LinearLayout.LayoutParams =
            LinearLayout.LayoutParams(WRAP_CONTENT,WRAP_CONTENT)
        layoutParams.setMargins(8,0,8,0)
        for (i in indicators.indices){
            indicators[i]= ImageView(applicationContext)
            indicators[i]?.let {
                it.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,R.drawable.onboarding_indicator_inactive_background
                    )
                )
                it.layoutParams = layoutParams
                indicatorContainer.addView(it)
            }
        }

    }

    private fun setCurrentIndicator(position:Int){
        val childCount = indicatorContainer.childCount
        for (i in 0 until  childCount){
            val imageView = indicatorContainer.getChildAt(i) as ImageView
            if(i == position){
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(applicationContext,R.drawable.onboarding_indicator_active_background)
                )
            }else{
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(applicationContext,R.drawable.onboarding_indicator_inactive_background)
                )

            }
        }
    }

}