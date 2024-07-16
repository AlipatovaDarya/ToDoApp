package com.example.todoapp3.data.room.entity

import androidx.room.TypeConverter
import java.time.LocalDate


/**
 * Auxiliary class for converting LocalDate to Timestamp and vice versa
 */
class DateConverter {
    @TypeConverter
    fun toLocalDate(timestamp: Long?): LocalDate? {
        return timestamp?.let { LocalDate.ofEpochDay(it) }
    }

    @TypeConverter
    fun toTimestamp(date: LocalDate?): Long? {
        return date?.toEpochDay()
    }
}