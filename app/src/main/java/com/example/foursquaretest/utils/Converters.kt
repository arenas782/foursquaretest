package com.example.foursquaretest.utils

import androidx.room.TypeConverter
import com.example.foursquaretest.data.model.responses.Category
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class Converters {
    @TypeConverter
    fun fromString(value: String?): List<Category>? {
        val listType: Type = object : TypeToken<List<Category?>?>() {}.type
        return Gson().fromJson<List<Category>>(value, listType)
    }

    @TypeConverter
    fun listToString(list: List<Category?>?): String? {
        val gson = Gson()
        return gson.toJson(list)
    }
}