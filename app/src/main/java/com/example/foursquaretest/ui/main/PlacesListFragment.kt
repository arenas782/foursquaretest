package com.example.foursquaretest.ui.main


import android.Manifest
import android.Manifest.permission
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender.SendIntentException
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.fragment.app.activityViewModels
import com.example.foursquaretest.R
import com.example.foursquaretest.databinding.FragmentListBinding
import com.example.foursquaretest.ui.main.viewmodel.NearbyPlacesViewModel
import com.example.foursquaretest.utils.BaseFragment
import com.example.foursquaretest.utils.Commons
import com.example.foursquaretest.utils.Status
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.PermissionRequestErrorListener
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit


@AndroidEntryPoint
class PlacesListFragment : BaseFragment(R.layout.fragment_list) {

    private lateinit var binding : FragmentListBinding

    private val viewModel: NearbyPlacesViewModel by activityViewModels()
    private var currentLocation: Location? = null
    private lateinit var locationRequest: LocationRequest

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private lateinit var locationManager: LocationManager
    var gpsStatus = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListBinding.inflate(inflater,container,false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("MissingPermission")
    private  fun getLastLocation(){
        fusedLocationProviderClient.lastLocation.addOnSuccessListener {
            it.let {
                currentLocation = it
                setupObserver(it)
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())


        Dexter.withContext(requireContext())
            .withPermissions(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION)
            .withListener(object : MultiplePermissionsListener {
                @SuppressLint("MissingPermission")
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    Log.e("permissions","granted")
                    getLastLocation()
                    fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())

                }


                override fun onPermissionRationaleShouldBeShown(
                    p0: MutableList<PermissionRequest>?,
                    p1: PermissionToken?
                ) {
                    TODO("Not yet implemented")
                }

            }).check()
        locationRequest = LocationRequest.create().apply {
            interval = TimeUnit.SECONDS.toMillis(2)
            fastestInterval = TimeUnit.SECONDS.toMillis(2)
            maxWaitTime = TimeUnit.MINUTES.toMillis(2)
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        locationCallback = object : LocationCallback() {
            @RequiresApi(Build.VERSION_CODES.M)
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                currentLocation = locationResult.lastLocation
                if (currentLocation != null){
                    setupObserver(currentLocation!!)
                }
            }
        }


    }

    private fun setupClickObserver(){
        viewModel.actionDetails.observe(viewLifecycleOwner){
            it.getContentIfNotHandled()?.let { action ->
                if (action) {
                    val intent = Intent(requireContext(), PlaceDetailActivity::class.java)
                    intent.putExtra("place",viewModel.place.value)
                    startActivity(intent)
                }
            }
        }
    }
    @RequiresApi(Build.VERSION_CODES.M)
    private fun setupObserver(location : Location){
        viewModel.searchPlaces(location.latitude.toString()+","+ location.longitude.toString()).observe(viewLifecycleOwner){ response ->
            when (response.status){
                Status.SUCCESS -> {
                    binding.rvResults.visibility = View.VISIBLE
                    binding.progressBar.visibility = View.GONE
                    binding.btnRetry.visibility = View.GONE
                    if(response.data?.results?.isEmpty() == true){
                        binding.tvNothingFound.visibility = View.VISIBLE
                    }
                    else{
                        setupClickObserver()
                        binding.tvNothingFound.visibility = View.GONE
                    }
                }
                Status.ERROR -> {
                    binding.rvResults.visibility = View.GONE
                    binding.progressBar.visibility = View.GONE
                    binding.tvNothingFound.visibility = View.GONE
                    binding.btnRetry.visibility = View.VISIBLE
                    Commons.showSnackBar(Commons.getString(R.string.something_went_wrong),binding.root)
                }
                Status.LOADING -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.rvResults.visibility = View.GONE
                    binding.tvNothingFound.visibility = View.GONE
                    binding.btnRetry.visibility = View.GONE
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun launchRequestLocation(){
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.e("permissions","not granted")
            return
        }
        else{
            Log.e("permissions","granted")
            getLastLocation()
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
        }

    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnGps.setOnClickListener {
            enableLoc()
        }
        binding.btnRetry.setOnClickListener {
            setupObserver(currentLocation!!)
        }

        if (gpsIsOn()){
            launchRequestLocation()
        }
        else{
            Log.e("GPS","is off")
            enableLoc()
        }


    }

    private fun enableLoc() {
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)

        LocationServices
            .getSettingsClient(requireActivity())
            .checkLocationSettings(builder.build())
            .addOnSuccessListener(requireActivity()) { response: LocationSettingsResponse? -> }
            .addOnFailureListener(requireActivity()) { ex ->
                if (ex is ResolvableApiException) {
                    try {
                        val resolvable = ex as ResolvableApiException
                        resolvable.startResolutionForResult(
                            requireActivity(),
                            100
                        )
                    } catch (sendEx: SendIntentException) {
                    }
                }
            }
    }


    private fun gpsIsOn() :Boolean {
        locationManager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        gpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        return gpsStatus
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        @Nullable data: Intent?
    ) {
        if (100 == requestCode) {
            if (Activity.RESULT_OK == resultCode) {
                if (ActivityCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return
                }
                else{
                    Log.e("TAG","user enabled GPS")
                    binding.btnGps.visibility = View.GONE
                    binding.btnRetry.visibility = View.GONE
                    binding.rvResults.visibility = View.GONE
                    getLastLocation()
                    fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
                }
            } else {
                Commons.showSnackBar("You need to enable GPS",binding.rvResults)
                binding.btnGps.visibility = View.VISIBLE
                binding.btnRetry.visibility = View.GONE
                binding.rvResults.visibility = View.GONE
            }
        }
    }
}