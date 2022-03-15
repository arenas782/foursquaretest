package com.example.foursquaretest.data.api

import com.example.foursquaretest.data.model.responses.NearbyResponse
import retrofit2.http.*

interface NearPlacesAPI {
    @GET("nearby")
    suspend fun getNearBySeattle(@Query ("query") query : String,@Query ("ll") latLong: String): NearbyResponse?
}