package com.example.foursquaretest.data.api

import com.example.foursquaretest.data.model.responses.NearbyResponse
import retrofit2.http.*

interface NearPlacesAPI {
    @GET("nearby")
    suspend fun getNearBySeattle(@Query ("ll") location: String,@Query("limit") limit : String): NearbyResponse?
}