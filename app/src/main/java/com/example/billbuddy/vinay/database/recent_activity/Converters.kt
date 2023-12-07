package com.example.billbuddy.vinay.database.recent_activity

import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun fromMapToString(map: Map<String,Int>): String = map.toString()
    @TypeConverter
    fun toMapFromString(stringList: String): HashMap<String,Int> {
        val result = HashMap<String,Int>()
        val split =stringList.replace("{","").replace("}","").replace(" ","").split(",")
        for (n in split) {
            val elements = n.split("=")
            try {
                result[elements[0]] = elements[1].toInt()
            } catch (e: Exception) {
            }
        }
        return result
    }
}