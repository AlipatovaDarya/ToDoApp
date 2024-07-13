package com.example.todoapp3.data.room.entity

import androidx.room.TypeConverter


/**
 * Auxiliary class for converting List<String> to String and vice versa
 */
class StringListConverter {
    @TypeConverter
    fun fromString(value: String?): List<String>? {
        return value?.let {
            it.split(",").toList()
        }
    }

    @TypeConverter
    fun toString(list: List<String>?): String? {
        return list?.joinToString(",")
    }
}