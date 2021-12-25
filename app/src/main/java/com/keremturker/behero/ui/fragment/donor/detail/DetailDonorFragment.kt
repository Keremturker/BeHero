package com.keremturker.behero.ui.fragment.donor.detail

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
import com.keremturker.behero.databinding.FragmentDetailDonorBinding
import com.keremturker.behero.model.Response
import com.keremturker.behero.model.Users
import com.keremturker.behero.utils.ToolbarType
import com.keremturker.behero.utils.extension.callPhone
import com.keremturker.behero.utils.extension.getAddress
import com.keremturker.behero.utils.extension.visibleIf
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailDonorFragment :
    BaseFragment<FragmentDetailDonorBinding, DetailDonorVM>(), OnMapReadyCallback {

    private var user: Users? = null
    override val viewModel: DetailDonorVM by viewModels()
    override var toolbarType = ToolbarType.Normal
    var currentMarker: Marker? = null

    override fun getViewBinding() = FragmentDetailDonorBinding.inflate(layoutInflater)

    override fun onFragmentCreated() {
        setNormalToolbar(
            isBackIcon = true,
            title = getString(R.string.find_donor_detail_title),
        )
        user = arguments?.getSerializable("users") as Users?

        setView(user)
    }

    override fun onResume() {
        super.onResume()
        user?.let {
            viewModel.getDonationCount(it.uuid)
        }
    }

    override fun observe() {
        viewModel.countDonation.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Response.Loading -> binding.layoutRequest.pbCount.visibleIf(true)
                is Response.Success -> {
                    binding.layoutRequest.pbCount.visibleIf(false)
                    binding.layoutRequest.txtContent.text = response.data.toString()
                    binding.layoutRequest.cvParent.setOnClickListener {
                        user?.let {
                            viewModel.goToDonationList(it)
                        }
                    }
                }
                is Response.Failure -> {
                    binding.layoutRequest.pbCount.visibleIf(false)
                    binding.layoutRequest.txtContent.text = "0"
                }
            }
        }
    }

    private fun setView(user: Users?) {
        user?.let { item ->
            binding.apply {

                setMap()

                txtName.text = item.name
                llShortAddress.visibleIf(item.address.getAddress().isNotEmpty())
                txtShortAddress.text = item.address.getAddress()

                btnCallNow.onClickLister {
                    requireContext().callPhone(item.phone)
                }

                layoutBloodType.txtTitle.text = getString(R.string.blood_type)
                layoutBloodType.txtContent.text = item.bloodGroup
                layoutRequest.txtTitle.text = getString(R.string.requested)

            }
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

        val latitude = user!!.address?.latitude
        val longitude = user!!.address?.longitude

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
}