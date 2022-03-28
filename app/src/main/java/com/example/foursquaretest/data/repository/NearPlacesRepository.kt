package com.example.foursquaretest.data.repository

import com.example.foursquaretest.data.api.NearPlacesAPI
import com.example.foursquaretest.data.model.responses.Place
import com.example.foursquaretest.data.room.PlaceDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NearPlacesRepository @Inject constructor(private val placesDao : PlaceDao, private val nearPlacesAPI: NearPlacesAPI) {


    suspend fun getNearbyPlaces(location : String) = nearPlacesAPI.getNearBySeattle(location =location,limit = "10")

    fun getFavoritePlaces(): Flow<List<Place>> = placesDao.getPlaces()


    suspend fun addFavoritePlace(place : Place){
        placesDao.insertFavoritePlace(place)
    }

    suspend fun deleteFavoritePlace(place : Place){
        placesDao.deleteFavoritePlace(place)
    }
}