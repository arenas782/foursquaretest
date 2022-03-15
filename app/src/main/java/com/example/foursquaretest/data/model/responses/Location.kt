package com.example.foursquaretest.data.model.responses

import com.squareup.moshi.Json

data class Location(
    @Json(name = "address")
    val address: String?,

    @Json(name = "formatted_address")
    val formatted_address: String?,

    @Json(name = "locality")
    val locality: String?,
)
