package com.example.foursquaretest.data.model.responses

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.*
import com.example.foursquaretest.utils.Converters
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Entity(tableName="places")
@Parcelize
data class Place(
    @PrimaryKey(autoGenerate = false)
    @NonNull
    @Json(name = "fsq_id")
    var fsq_id: String,

    @Json(name = "distance")
    var distance: Double?,

    @Json(name = "name")
    var name: String?,

    @Json(name = "location")
    @Embedded(prefix ="place")
    var location: Location?,

    @Json(name = "geocodes")
    @Embedded(prefix ="place")
    var geocodes: Geocodes?,

    @Json(name = "categories")
    @ColumnInfo(name = "categories")
    @TypeConverters(Converters::class)
    var categories: List<Category?>?,

):Parcelable{
}
