package com.example.foursquaretest.data.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.foursquaretest.data.model.responses.Place
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaceDao {
    @Query("SELECT * FROM places")
    fun getPlaces(): Flow<List<Place>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoritePlace(place : Place)

    @Delete
    suspend fun deleteFavoritePlace(place: Place)
}