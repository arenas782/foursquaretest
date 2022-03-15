package com.example.foursquaretest.data.model.responses

import com.squareup.moshi.Json

data class NearbyResponse (
    @Json(name = "results")
    val results: List<Place>?,
)