package com.example.foursquaretest.data.model.responses

import android.os.Parcelable
import androidx.room.Entity
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class Location(
    @Json(name = "address")
    val address: String?,

    @Json(name = "formatted_address")
    val formatted_address: String?,

    @Json(name = "locality")
    val locality: String?,
) : Parcelable
