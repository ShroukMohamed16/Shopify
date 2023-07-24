package com.example.shopify.address.ui.view

import android.app.AlertDialog
import android.content.Intent
import android.location.Geocoder
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.example.shopify.R
import com.example.shopify.utilities.MyLocation
import com.example.shopify.utilities.MySharedPreferences

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch
import java.util.*

class MapsFragment : Fragment() {

    private val callback = OnMapReadyCallback { map ->
        val googleMap = map

        //handle intial map
            googleMap.setOnMapClickListener { latLng ->
                val geocoder =
                    Geocoder(requireContext(), Locale.getDefault())
                val addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
                if (addresses!!.isNotEmpty()) {
                    val address = addresses[0]
                    val city = address.locality
                    if (!city.isNullOrEmpty()) {
                        val builder = AlertDialog.Builder(requireContext())
                        builder.setMessage(
                            "${context?.getString(R.string.messageHomeMapAlert)} $city"
                        )
                        builder.setPositiveButton("${context?.getString(R.string.yesMapAlert)}") { _, _ ->
                            MyLocation.lat = latLng.latitude
                            MyLocation.lng = latLng.longitude
                            Navigation.findNavController(requireView())
                                .navigate(R.id.action_mapsFragment_to_addAddressFragment)
                        }
                        builder.setNegativeButton("${context?.getString(R.string.CancelMapAlert)}") { _, _ -> }
                        val dialog = builder.create()
                        dialog.show()
                    } else {
                        Toast.makeText(requireContext(), "${context?.getString(R.string.chooseSpecificPlace)}", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(requireContext(), "${context?.getString(R.string.errorFavMapAlert)}", Toast.LENGTH_SHORT).show()
                }
            }



        val alex = LatLng(23.22, 33.3)
        googleMap.addMarker(MarkerOptions().position(alex).title("Marker"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(alex))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }
}