package com.example.foursquaretest.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.foursquaretest.data.model.responses.Place
import com.example.foursquaretest.utils.Converters


@Database(
    entities = [
        Place::class
    ], version = 1, exportSchema = false
)
@TypeConverters(
    Converters::class
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun placeDao(): PlaceDao

    companion object {
        const val DATABASE_NAME : String = "places_db"
    }
}