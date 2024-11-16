package com.maran.test.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type


class DataConverter {
    @TypeConverter
    fun fromList(countryLang: List<String?>?): String? {
        if (countryLang == null) {
            return (null)
        }
        val gson = Gson()
        val type: Type = object : TypeToken<List<String?>?>() {}.type
        val json: String = gson.toJson(countryLang, type)
        return json
    }

    @TypeConverter
    fun toList(countryLangString: String?): List<String>? {
        if (countryLangString == null) {
            return (null)
        }
        val gson = Gson()
        val type: Type = object : TypeToken<List<String?>?>() {}.type
        val countryLangList: List<String> = gson.fromJson(countryLangString, type)
        return countryLangList
    }
}