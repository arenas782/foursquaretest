package com.example.foursquaretest.data.repository

import com.example.foursquaretest.data.api.NearPlacesAPI
import javax.inject.Inject

class NearPlacesRepository @Inject constructor(private val nearPlacesAPI: NearPlacesAPI) {


    suspend fun getNearbyPlaces(query : String) = nearPlacesAPI.getNearBySeattle(query,latLong = "47.6062,-122.3321")


}