package com.example.foursquaretest.data.model.responses

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.TypeConverters
import com.example.foursquaretest.utils.Converters
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class Category(
    @Json(name = "id")
    val id: Int?,

    @Json(name = "name")
    val name: String?,

    @Json(name = "icon")
    @Embedded(prefix = "icon")
    val icon: Icon?,

) : Parcelable
