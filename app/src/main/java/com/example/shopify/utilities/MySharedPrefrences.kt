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
    fun saveCountryName(name:String){
        sharedPreferences.edit().putString(Constants.COUNTRY_NAME, name).apply()
    }
    fun getCountryName(): String? {
        return sharedPreferences.getString(Constants.COUNTRY_NAME,"")
    }
    fun saveCityName(name:String){
        sharedPreferences.edit().putString(Constants.CITY_NAME, name).apply()
    }
    fun getCityName(): String? {
        return sharedPreferences.getString(Constants.CITY_NAME,"")
    }
    fun saveProvinceName(name:String){
        sharedPreferences.edit().putString(Constants.PROVINCE_NAME, name).apply()
    }
    fun getProvinceName(): String? {
        return sharedPreferences.getString(Constants.PROVINCE_NAME,"")
    }
    fun saveZipCode(name:String){
        sharedPreferences.edit().putString(Constants.ZIP_CODE, name).apply()
    }
    fun getZipCode(): String? {
        return sharedPreferences.getString(Constants.ZIP_CODE,"")
    }
}