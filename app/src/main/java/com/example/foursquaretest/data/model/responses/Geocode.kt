package com.example.foursquaretest.data.model.responses

import com.squareup.moshi.Json

data class Geocode(
    @Json(name = "latitude")
    val latitude: Double?,

    @Json(name = "longitude")
    val longitude: Double?,
)
