package com.example.shopify.utilities

import android.app.AlertDialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.SharedPreferences
import android.location.Geocoder
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import com.example.shopify.R
import java.util.*

fun checkConnectivity(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    var networkCapabilities: NetworkCapabilities? = null
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        networkCapabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
    }
    val isConnected =
        networkCapabilities != null && networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    return isConnected
}

object MyLocation {
    private var sharedPreferences: SharedPreferences? = null
    var lat: Double? = null
        set(value) {
            field = value
        }
    var lng: Double? = null
        set(value) {
            field = value
        }
}
object MyAddress{
    var city: String? = null
        set(value) {
            field = value
        }
    var country: String? = null
        set(value) {
            field = value
        }
    var zipCode: String? = null
        set(value) {
            field = value
        }
    var province: String? = null
        set(value) {
            field = value
        }
}
fun getAddress(context: Context, latitude: Double, longitude: Double) {
    val geocoder = Geocoder(context, Locale.getDefault())
    val addresses = geocoder.getFromLocation(latitude, longitude, 1)
    if (addresses != null) {
        if (addresses.isNotEmpty()) {
            val address = addresses[0]
            val city = address.locality
            val country = address.countryName
            val province = address.adminArea
            val zipCode = address.postalCode
            Log.i("TAG", "getAddress: city $city")

            Log.i("TAG", "getAddress: country $country")

            Log.i("TAG", "getAddress: province $province")

            Log.i("TAG", "getAddress: zipCode $zipCode")
            MyAddress.city=city
            MyAddress.country=country
            MyAddress.province=province
            MyAddress.zipCode=zipCode
        }
    }
}
fun copyToClipboard(text: String, context: Context) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("text", text)
    clipboard.setPrimaryClip(clip)
}
fun  setAppLanguage(language:String) {
    val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags(language)
    AppCompatDelegate.setApplicationLocales(appLocale)
}


fun createAlert(title: String, message: String,context: Context) {
    val builder = AlertDialog.Builder(context)
    builder.setTitle(title)
    builder.setMessage(message)
    builder.setPositiveButton("${context?.getString(R.string.ok)}") { dialog, which ->
    }
    val alertDialog = builder.create()
    alertDialog.show()
}
