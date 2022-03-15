package com.example.foursquaretest.data.model.responses

import com.squareup.moshi.Json

data class Place(
    @Json(name = "fsq_id")
    val fsq_id: String?,

    @Json(name = "distance")
    val distance: Double?,

    @Json(name = "name")
    val name: String?,

    @Json(name = "location")
    val location: Location?,

    @Json(name = "geocodes")
    val geocodes: Geocodes?,

    @Json(name = "categories")
    val categories: List<Category?>?,

)
