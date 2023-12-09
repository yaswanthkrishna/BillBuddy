package com.example.billbuddy.vinay.database.sharedpreferences

import android.content.Context
import android.content.SharedPreferences

class PreferenceHelper(val context: Context) {

    companion object {
        var sharedPreferences: SharedPreferences? = null
    }

    fun sharedPreference(): SharedPreferences? {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences("login_pref", Context.MODE_PRIVATE)
        }
        return sharedPreferences
    }

    fun writeLongToPreference(key: String?, value: Long) {
        sharedPreference()
        val editor = sharedPreferences!!.edit()
        editor.putLong(key, value)
        editor.apply()
    }

    fun writeBooleanToPreference(key: String?, value: Boolean) {
        sharedPreference()
        val editor = sharedPreferences!!.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    fun readBooleanFromPreference(key: String?): Boolean {
        sharedPreference()
        return sharedPreferences!!.getBoolean(key, true)
    }

    fun writeStringToPreference(key: String?, value: String) {
        sharedPreference()
        val editor = sharedPreferences!!.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun readStringFromPreference(key: String?): String {
        sharedPreference()
        return sharedPreferences!!.getString(key, "").toString()
    }

    fun readLongFromPreference(key: String?): Long {
        sharedPreference()
        return sharedPreferences!!.getLong(key, 0L)
    }
}
