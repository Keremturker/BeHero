package com.keremturker.behero.ui.fragment.donation.detail

import androidx.fragment.app.viewModels
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.keremturker.behero.R
import com.keremturker.behero.base.BaseFragment
import com.keremturker.behero.databinding.FragmentDetailDonationBinding
import com.keremturker.behero.model.Donations
import com.keremturker.behero.utils.Constants.DONATION
import com.keremturker.behero.utils.SharedHelper
import com.keremturker.behero.utils.ToolbarType
import com.keremturker.behero.utils.extension.getAddress
import com.keremturker.behero.utils.extension.getBloodImage
import com.keremturker.behero.utils.extension.getNavigationResultLiveData
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DetailDonationFragment :
    BaseFragment<FragmentDetailDonationBinding, DetailDonationVM>(), OnMapReadyCallback {

    override val viewModel: DetailDonationVM by viewModels()
    override fun getViewBinding() = FragmentDetailDonationBinding.inflate(layoutInflater)

    private var donation: Donations? = null
    override var toolbarType = ToolbarType.Normal
    var currentMarker: Marker? = null


    @Inject
    lateinit var sharedHelper: SharedHelper

    override fun onFragmentCreated() {
        donation = arguments?.getSerializable("donation") as Donations?

        if (sharedHelper.syncUsers?.uuid == donation?.uuid) {
            setNormalToolbar(
                isBackIcon = true,
                title = getString(R.string.donation_detail_title),
                rightIcon = R.drawable.ic_edit
            ) {
                donation?.let {
                    viewModel.goToDonationEdit(it)
                }
            }
        } else {
            setNormalToolbar(isBackIcon = true, title = getString(R.string.donation_detail_title))
        }

    }

    private fun setView(donation: Donations?) {
        donation?.let {
            binding.edtPatientName.setText(it.patientName)
            binding.edtHospitalName.setText(it.hospitalName)
            binding.imgBloodGroup.setBackgroundResource(donation.bloodGroup.getBloodImage())
            binding.edtDescription.setText(donation.description)
            binding.edtAddress.setText(donation.address.getAddress())

            setMap()

        }
    }

    private fun setMap() {
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
    }


    override fun onMapReady(googleMap: GoogleMap) {
        googleMap.clear()
        googleMap.uiSettings.setAllGesturesEnabled(false)

        val latitude = donation!!.address.latitude
        val longitude = donation!!.address.longitude

        if (latitude != null && longitude != null) {
            val latLng = LatLng(latitude, longitude)
            moveMarket(googleMap, latLng)
        }

    }

    private fun moveMarket(googleMap: GoogleMap, latLng: LatLng) {
        val markerOptions = MarkerOptions().position(latLng).draggable(true)
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng))
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
        currentMarker = googleMap.addMarker(markerOptions)
        currentMarker?.showInfoWindow()
    }

    override fun onResume() {
        super.onResume()
        val newDonation = this.getNavigationResultLiveData<Donations>(DONATION)
        newDonation?.value?.let {
            donation = it
            setView(it)
        } ?: setView(donation)
    }
}