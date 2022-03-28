package com.example.foursquaretest.ui.main


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.foursquaretest.R
import com.example.foursquaretest.data.model.responses.Place
import com.example.foursquaretest.databinding.ActivityFavoriteDetailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteDetailActivity : AppCompatActivity(R.layout.activity_favorite_detail){

    private lateinit var binding : ActivityFavoriteDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_favorite_detail)
        binding.lifecycleOwner = this
        val place = intent.getParcelableExtra("place") as? Place
        binding.place = place
    }
}
