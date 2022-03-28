package com.example.foursquaretest.ui.main

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.foursquaretest.R
import com.example.foursquaretest.data.model.responses.Place
import com.example.foursquaretest.databinding.ActivityPlaceDetailBinding
import com.example.foursquaretest.ui.main.viewmodel.NearbyPlacesViewModel
import com.example.foursquaretest.utils.Commons
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlaceDetailActivity : AppCompatActivity(R.layout.activity_place_detail){

    private lateinit var binding : ActivityPlaceDetailBinding
    private val viewModel: NearbyPlacesViewModel by lazy {
        ViewModelProvider(this)[NearbyPlacesViewModel::class.java]
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_place_detail)
        binding.lifecycleOwner = this
        val place = intent.getParcelableExtra("place") as? Place
        binding.place = place


        binding.ivFavorite.setOnClickListener {
            if (place != null) {
                Log.e("TAG","saving place")
                viewModel.saveFavoritePlace(place)
                Commons.showSnackBar(Commons.getString(R.string.added_to_favorites),binding.ivFavorite)
            }
        }
    }
}
