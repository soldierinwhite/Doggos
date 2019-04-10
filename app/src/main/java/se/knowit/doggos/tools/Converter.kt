package se.knowit.doggos.tools

import androidx.room.TypeConverter

object Converter {
    @JvmStatic
    @TypeConverter
    fun fromString(value: String): List<String> {
        return value.split(";")
    }

    @JvmStatic
    @TypeConverter
    fun fromListOfInts(strings: List<String>?): String {
        return strings?.joinToString(";") ?: ""
    }
}
