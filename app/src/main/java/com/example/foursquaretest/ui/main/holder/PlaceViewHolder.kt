package com.example.foursquaretest.ui.main.holder

import androidx.recyclerview.widget.RecyclerView
import com.example.foursquaretest.data.model.responses.Place
import com.example.foursquaretest.databinding.ItemPlaceBinding

class PlaceViewHolder(private val binding : ItemPlaceBinding) : RecyclerView.ViewHolder(binding.root) {


    fun bind(data: Place) {
        binding.place = data
        binding.executePendingBindings()
    }
}