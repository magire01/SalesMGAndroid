package com.mg.barpos.data

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
@ProvidedTypeConverter
class Converter {

    @TypeConverter
    fun toStringData(array: Array<String>): String {
        if (array.isEmpty()) {
            return ""
        } else {
            return array.joinToString(",")
        }
    }

    @TypeConverter
    fun toArray(string: String): Array<String> {
        if (string.isEmpty()) {
            return emptyArray()
        } else {
            return string.split(",").toTypedArray()

        }
    }

}