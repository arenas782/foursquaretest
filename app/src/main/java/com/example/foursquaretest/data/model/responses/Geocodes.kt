package com.example.foursquaretest.data.model.responses

import com.squareup.moshi.Json

data class Geocodes(
    @Json(name = "main")
    val main: Geocode?,

    @Json(name = "roof")
    val roof: Geocode?,
)
