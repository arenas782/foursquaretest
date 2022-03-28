package com.example.foursquaretest.ui.main.viewmodel

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.*
import com.example.foursquaretest.data.model.responses.Place
import com.example.foursquaretest.data.repository.NearPlacesRepository
import com.example.foursquaretest.ui.main.adapter.PlaceAdapter
import com.example.foursquaretest.utils.EventLiveData
import com.example.foursquaretest.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class NearbyPlacesViewModel @Inject constructor(private val repository: NearPlacesRepository) : ViewModel()  {


    val placesAdapter = PlaceAdapter(deleteCallback = {}) {
        _place.value = it
        _actionDetails.value =EventLiveData( true)
    }
    val favoritesAdapter = PlaceAdapter (isFavoriteList = true,
        deleteCallback = {
            deleteFavoritePlace(it)
        }
    ){

        _favoritePlace.value = it
        _actionDetailsFavorite.value = EventLiveData(true)
    }

    private var _actionDetails = MutableLiveData<EventLiveData<Boolean>>()
    var actionDetails: LiveData<EventLiveData<Boolean>> = _actionDetails

    private var _actionDetailsFavorite = MutableLiveData<EventLiveData<Boolean>>()
    var actionDetailsFavorite: LiveData<EventLiveData<Boolean>> = _actionDetailsFavorite

    private var _place = MutableLiveData<Place>()
    val place : LiveData<Place> = _place

    private var _favoritePlace = MutableLiveData<Place>()
    val favoritePlace : LiveData<Place> = _favoritePlace


    val _places = MutableLiveData<MutableList<Place>> ()
    var places: LiveData<MutableList<Place>> = _places

    val _favorites = MutableLiveData<MutableList<Place>> ()
    private val _isLoading = MutableLiveData(false)
    val isLoading : LiveData<Boolean> = _isLoading





    fun searchPlaces(location : String) = liveData(Dispatchers.Main) {
        emit(Resource.loading(data = null))
        _isLoading.value = false
        try {
            val data = repository.getNearbyPlaces(location)
            updatePlaces(data?.results)
            emit(Resource.success(data = data))
            _isLoading.value = true
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.stackTraceToString() ?: "Error Occurred!"))
            _isLoading.value = false
        }
    }

    fun saveFavoritePlace(place : Place) {
        viewModelScope.launch {
            try {
                Log.e("ERROR","trying to save")
                repository.addFavoritePlace(place)
            } catch (exception: Exception) {
                Log.e("ERROR",exception.stackTraceToString())

            }
        }
    }

    private fun deleteFavoritePlace(place : Place) {
        viewModelScope.launch {
            try {
                Log.e("ERROR","trying to save")
                repository.deleteFavoritePlace(place)

            } catch (exception: Exception) {
                Log.e("ERROR",exception.stackTraceToString())
            }
        }
    }

    private suspend fun favoritesFlow(): Flow<List<Place>> = repository.getFavoritePlaces()

    fun getFavorites() {
        viewModelScope.launch {

            try {
                favoritesFlow().collect {
                    updateFavorites(it)
                    Log.e("Places",it.toString())
                }
            } catch (exception: Exception) {
                Log.e("TAG",exception.stackTraceToString())
            }
        }



    }

    @SuppressLint("NotifyDataSetChanged")
    private fun updatePlaces(list: List<Place>?){
        val aux = mutableListOf<Place>()
        _places.value = aux
        if (list != null) {
            for (item in list) {
                aux.add(item)
            }
            _places.value!!.addAll(aux)
            _places.notifyObserver()
        }
        placesAdapter.updateData(aux)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun updateFavorites(list: List<Place>?){
        _favorites.value = arrayListOf()

        if (list != null) {
            _favorites.value!!.addAll(list)
            _favorites.notifyObserver()
        }
        favoritesAdapter.updateData(list as MutableList<Place>)
    }
    private fun <T> MutableLiveData<T>.notifyObserver() {
        this.value = this.value
    }
}