package com.example.foursquaretest.data.model.responses

import android.os.Parcelable
import androidx.room.Entity
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class Icon(
    @Json(name = "prefix")
    val prefix: String?,

    @Json(name = "suffix")
    val suffix: String?,
) : Parcelable
