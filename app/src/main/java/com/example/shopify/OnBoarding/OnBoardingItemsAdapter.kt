package com.example.shopify.OnBoarding

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintSet.Layout
import androidx.recyclerview.widget.RecyclerView
import com.example.shopify.R

class OnBoardingItemsAdapter(private val onBoardingItemList:List<OnBoardingItem>):RecyclerView.Adapter<OnBoardingItemsAdapter.OnBoardingItemViewHolder>()
{

    inner class OnBoardingItemViewHolder(view:View):RecyclerView.ViewHolder(view){
        private val imageOnboarding = view.findViewById<ImageView>(R.id.image_onboarding_container)
    /*    private val textTitle = view.findViewById<TextView>(R.id.txt_title_onboarding_container)*/
        private val textDescription = view.findViewById<TextView>(R.id.txt_description_onboarding_container)

        fun bind(onBoardingItem: OnBoardingItem){
            imageOnboarding.setImageResource(onBoardingItem.onBoardingImage)
           /* textTitle.text=onBoardingItem.onBoardingTitle*/
            textDescription.text=onBoardingItem.onBoardingDescription
        }



    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnBoardingItemViewHolder {
        return OnBoardingItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.onboarding_item_container,parent,false))
    }

    override fun onBindViewHolder(holder: OnBoardingItemViewHolder, position: Int) {
        holder.bind(onBoardingItemList[position])
    }

    override fun getItemCount(): Int {
       return onBoardingItemList.size
    }
}