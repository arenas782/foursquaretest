package com.example.foursquaretest.ui.main.viewmodel

import android.annotation.SuppressLint
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.foursquaretest.data.model.responses.Place
import com.example.foursquaretest.data.repository.NearPlacesRepository
import com.example.foursquaretest.ui.main.adapter.PlaceAdapter
import com.example.foursquaretest.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject


@HiltViewModel
class NearbyPlacesViewModel @Inject constructor(private val repository: NearPlacesRepository) : ViewModel()  {


    val placesAdapter = PlaceAdapter ()

    private val _places: MutableList<Place> = arrayListOf()

    private val _isLoading = MutableLiveData(false)
    val isLoading : LiveData<Boolean> = _isLoading


    fun clearPlaces(){
        _places.clear()
        placesAdapter.updateData(_places)
    }


    fun searchPlaces(query : String) = liveData(Dispatchers.Main) {
        emit(Resource.loading(data = null))
        try {
            val data = repository.getNearbyPlaces(query)
            updatePlaces(data?.results)
            emit(Resource.success(data = data))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.stackTraceToString() ?: "Error Occurred!"))
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun updatePlaces(list: List<Place>?){
        _places.clear()

        if (list != null) {
            for (item in list)
                _places.add(item)
        }
        placesAdapter.updateData(_places)
    }



}