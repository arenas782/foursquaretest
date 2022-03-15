package com.example.foursquaretest.data.model.responses

import com.squareup.moshi.Json

data class Category(
    @Json(name = "id")
    val id: Int?,

    @Json(name = "name")
    val name: String?,

    @Json(name = "icon")
    val icon: Icon?,

)
