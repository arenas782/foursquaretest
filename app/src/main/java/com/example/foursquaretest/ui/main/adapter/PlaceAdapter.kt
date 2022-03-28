package com.example.foursquaretest.ui.main.adapter


import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.foursquaretest.R
import com.example.foursquaretest.data.model.responses.Place
import com.example.foursquaretest.databinding.ItemPlaceBinding
import com.example.foursquaretest.ui.main.holder.PlaceViewHolder

class PlaceAdapter(private val isFavoriteList : Boolean = false,private val deleteCallback : (place : Place) -> Unit,private val setupClickCallback: (place : Place) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var places: MutableList<Place>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding: ItemPlaceBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            viewType,
            parent,
            false
        )
        return PlaceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is PlaceViewHolder -> {
                if(isFavoriteList){
                    holder.binding.tvDelete.visibility = View.VISIBLE
                    holder.binding.tvDelete.setOnClickListener {
                        deleteCallback(places[position])
                    }
                }

                holder.bind(places[position])
                holder.binding.cvPlace.setOnClickListener {
                    setupClickCallback(places[position])
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int = R.layout.item_place


    override fun getItemCount(): Int =
        if (::places.isInitialized) places.size else 0

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newPlaces: MutableList<Place>) {
        this.places = newPlaces
        notifyDataSetChanged()
    }
}