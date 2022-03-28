package com.example.foursquaretest.ui.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.foursquaretest.R
import com.example.foursquaretest.data.model.responses.Place
import com.example.foursquaretest.databinding.FragmentMapBinding
import com.example.foursquaretest.ui.main.viewmodel.NearbyPlacesViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapFragment : Fragment(R.layout.fragment_map) ,OnMapReadyCallback{

    private lateinit var binding : FragmentMapBinding
    private lateinit var mGoogleMap: GoogleMap

    private val viewModel: NearbyPlacesViewModel by activityViewModels()

    companion object {
        var mapFragment : SupportMapFragment?=null
        fun newInstance() = MapFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMapBinding.inflate(inflater,container,false)
        binding.lifecycleOwner = this
        mapFragment = childFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.isLoading.observe(viewLifecycleOwner){
            if (it == true){
                viewModel.places.value?.let { places -> setMarkers(places) }
            }
        }
    }
    private fun setMarkers(places : MutableList<Place>){
        mGoogleMap.clear()
        places.forEach {
            val latLng = LatLng(it.geocodes?.main?.latitude!!, it.geocodes?.main?.longitude!!)
            val markerOptions = MarkerOptions()
            markerOptions.position(latLng)
            markerOptions.title(it.name)
            mGoogleMap.addMarker(markerOptions)
        }
        val latLng = LatLng(places[0].geocodes?.main?.latitude!!, places[0].geocodes?.main?.longitude!!)
        val markerOptions = MarkerOptions()
        markerOptions.position(latLng)
        mGoogleMap.addMarker(markerOptions)
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12.0F))
    }
    override fun onMapReady(p0: GoogleMap) {
        mGoogleMap = p0
        mGoogleMap.mapType = GoogleMap.MAP_TYPE_NORMAL
    }
}