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

    private val sharedPreferences =
        context.getSharedPreferences(Constants.PREFERENCE_FILE_NAME, Context.MODE_PRIVATE)

    fun saveCouponCode(code: String) {
        sharedPreferences.edit().putString(Constants.PREFERENCE_COUPON_CODE, code).apply()
    }

    fun getCouponCode(): String? {
        return sharedPreferences.getString(Constants.PREFERENCE_COUPON_CODE, null)
    }
    fun saveCustomerID(id: Long) {
        sharedPreferences.edit().putLong(Constants.CUSTOMER_ID, id).apply()
    }
    fun getCartID(): Long? {
        return sharedPreferences.getLong(Constants.CART_DRAFT_ID, 0)
    }
    fun saveCartID(id: Long) {
        sharedPreferences.edit().putLong(Constants.CART_DRAFT_ID, id).apply()
    }
    fun getFavID(): Long? {
        return sharedPreferences.getLong(Constants.FAV_DRAFT_ID, 0)
    }
    fun saveFavID(id: Long) {
        sharedPreferences.edit().putLong(Constants.FAV_DRAFT_ID, id).apply()
    }

    fun getCustomerID(): Long? {
        return sharedPreferences.getLong(Constants.CUSTOMER_ID, 0)
    }

    fun saveOnBoardingState(state: Boolean) {
        sharedPreferences.edit().putBoolean(Constants.ONBOARDING_FLAG, state).apply()
    }

    fun getOnBoardingState(): Boolean {
        return sharedPreferences.getBoolean(Constants.ONBOARDING_FLAG, false)
    }

    fun saveCurrencyCode(code: String) {
        sharedPreferences.edit().putString(Constants.CURRENCY_CODE, code).apply()
    }

    fun getCurrencyCode(): String? {
        return sharedPreferences.getString(Constants.CURRENCY_CODE, null)
    }

    fun saveExchangeRate(rate :Float){
        sharedPreferences.edit().putFloat(Constants.EXCHANGE_RATE , rate).apply()
    }
    fun getExchangeRate() : Float{
        return sharedPreferences.getFloat(Constants.EXCHANGE_RATE , 0.0F)
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
    fun saveLanguagePreference(language: String) {
        sharedPreferences.edit().putString(Constants.LANGUAGE, language).apply()
    }

    fun getLanguagePreference(): String? {
        return sharedPreferences.getString(Constants.LANGUAGE, Constants.ENGLISH)
    }

    fun saveCustomerEmail(language: String) {
        sharedPreferences.edit().putString(Constants.CUSTOMER_EMAIL, language).apply()
    }

    fun getCustomerEmail(): String? {
        return sharedPreferences.getString(Constants.CUSTOMER_EMAIL, Constants.ENGLISH)
    }

    fun saveCustomerFirstName(name:String){
        sharedPreferences.edit().putString(Constants.customer_FN, name).apply()
    }
    fun getCustomerFirstName(): String? {
        return sharedPreferences.getString(Constants.customer_FN,"")
    }

    fun saveCustomerLastName(name:String){
        sharedPreferences.edit().putString(Constants.customer_LN, name).apply()
    }
    fun getCustomerLastName(): String? {
        return sharedPreferences.getString(Constants.customer_LN,"")
    }
}