package com.example.shopify.utilities

import android.content.Context


class MySharedPreferences private constructor(context: Context) {

    companion object {
        @Volatile
        private var INSTANCE: MySharedPreferences? = null

        fun getInstance(context: Context): MySharedPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = MySharedPreferences(context.applicationContext)
                INSTANCE = instance
                instance
            }
        }
    }

    private val sharedPreferences = context.getSharedPreferences(Constants.PREFERENCE_FILE_NAME, Context.MODE_PRIVATE)

    fun saveCouponCode(code: String) {
        sharedPreferences.edit().putString(Constants.PREFERENCE_COUPON_CODE, code).apply()
    }

    fun getCouponCode(): String? {
        return sharedPreferences.getString(Constants.PREFERENCE_COUPON_CODE, null)
    }

    fun saveOnBoardingState(state:Boolean){
        sharedPreferences.edit().putBoolean(Constants.ONBOARDING_FLAG, state).apply()
    }
    fun getOnBoardingState(): Boolean {
        return sharedPreferences.getBoolean(Constants.ONBOARDING_FLAG,false)
    }
}