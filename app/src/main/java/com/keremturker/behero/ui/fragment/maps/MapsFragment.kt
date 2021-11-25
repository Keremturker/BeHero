package com.keremturker.behero.ui.fragment.maps


import android.annotation.SuppressLint
import android.location.Geocoder
import android.location.Location
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.keremturker.behero.R
import com.keremturker.behero.base.BaseFragment
import com.keremturker.behero.databinding.FragmentMapsBinding
import com.keremturker.behero.model.Address
import com.keremturker.behero.utils.Constants.ADDRESS
import com.keremturker.behero.utils.Constants.PERMISSION_LOCATION
import com.keremturker.behero.utils.Constants.permissionLocation
import com.keremturker.behero.utils.extension.setNavigationResult
import java.io.IOException
import java.util.*

class MapsFragment : BaseFragment<FragmentMapsBinding, MapsVM>(), OnMapReadyCallback {


    var currentMarker: Marker? = null
    var currentAddress = Address()
    private lateinit var mMap: GoogleMap
    private var currentLocation: Location? = null
    private var fusedLocationProviderClient: FusedLocationProviderClient? = null


    override val viewModel: MapsVM by viewModels()

    override fun getViewBinding() = FragmentMapsBinding.inflate(layoutInflater)

    override fun onFragmentCreated() {

        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())

        requestPermission(PERMISSION_LOCATION, *permissionLocation)
        binding.imgCurrentLocation.setOnClickListener {
            fetchLocation()
        }

        binding.btnPick.setOnClickListener {
            this.setNavigationResult(currentAddress, ADDRESS)
            findNavController().navigateUp()
        }
    }


    override fun onPermissionGranted(permissions: Array<String>) {
        fetchLocation()
    }

    @SuppressLint("MissingPermission")
    fun fetchLocation() {
        val task = fusedLocationProviderClient!!.lastLocation
        task.addOnSuccessListener { location ->
            if (location != null) {
                currentLocation = location

                val mapFragment =
                    childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
                mapFragment?.getMapAsync(this)

            }
        }
    }


    override fun onPermissionDenied(permissions: Array<String>) {
        val showRationale = shouldShowRequestPermissionRationale(permissions[0])
        //check never ask again
        if (!showRationale) {
            Toast.makeText(
                requireContext(),
                getString(R.string.permission_denied),
                Toast.LENGTH_SHORT
            )
                .show()
        } else {
            requestPermission(
                PERMISSION_LOCATION,
                *permissionLocation
            )
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.clear()

        val latLng = LatLng(currentLocation!!.latitude, currentLocation!!.longitude)
        moveMarket(latLng)

        googleMap.setOnMarkerDragListener(object : GoogleMap.OnMarkerDragListener {
            override fun onMarkerDragStart(marker: Marker) {}
            override fun onMarkerDragEnd(marker: Marker) {

                if (currentMarker != null) {
                    currentMarker?.remove()
                }
                val newLatLng = LatLng(marker.position.latitude, marker.position.longitude)
                moveMarket(newLatLng)
            }

            override fun onMarkerDrag(marker: Marker) {}
        })

        googleMap.setOnMapClickListener {
            if (currentMarker != null) {
                currentMarker?.remove()
            }
            val newLatLng = LatLng(it.latitude, it.longitude)
            moveMarket(newLatLng)
        }
    }

    private fun moveMarket(latLng: LatLng) {

        currentAddress = Address(getTheAddress(latLng.latitude, latLng.longitude),latLng.latitude,latLng.longitude)
        val markerOptions = MarkerOptions().position(latLng)/*.title("I am here")
            .snippet(address)*/.draggable(true)
        binding.txtCurrentAddress.text = currentAddress.description
        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng))
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
        currentMarker = mMap.addMarker(markerOptions)
        currentMarker?.showInfoWindow()
    }

    private fun getTheAddress(latitude: Double, longitude: Double): String {
        var retVal = ""
        val geocoder = Geocoder(requireContext(), Locale.getDefault())
        try {
            val addresses = geocoder.getFromLocation(latitude, longitude, 1)
            retVal = addresses[0].getAddressLine(0)

        } catch (e: IOException) {
            e.printStackTrace()
        }
        return retVal
    }


}