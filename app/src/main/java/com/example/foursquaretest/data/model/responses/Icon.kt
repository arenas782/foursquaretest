package com.example.foursquaretest.data.model.responses

import com.squareup.moshi.Json

data class Icon(
    @Json(name = "prefix")
    val prefix: String?,

    @Json(name = "suffix")
    val suffix: String?,
)
