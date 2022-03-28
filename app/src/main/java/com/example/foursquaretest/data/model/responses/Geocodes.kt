package com.example.foursquaretest.data.model.responses

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class Geocodes(
    @Json(name = "main")
    @Embedded(prefix ="main")
    val main: Geocode?,

    @Json(name = "roof")
    @Embedded(prefix ="roof")
    val roof: Geocode?,
) : Parcelable
