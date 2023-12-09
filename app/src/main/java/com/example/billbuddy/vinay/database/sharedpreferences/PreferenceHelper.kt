package com.example.billbuddy.vinay.database.sharedpreferences

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity

class PreferenceHelper(val context: Context) {

    companion object {
        var sharedPreferences: SharedPreferences? = null
    }
    fun clearUserSession() {
        sharedPreference()?.edit()?.apply {
            clear() // This clears all the data, use remove("USER_ID") if you want to clear specific data
            apply()
        }
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
    private val preferences: SharedPreferences = context.getSharedPreferences("YourPreferenceName", Context.MODE_PRIVATE)

    fun readLongFromPreference(key: String, defaultValue: Long = 0L): Long {
        return preferences.getLong(key, defaultValue)
    }
    fun writeActivityToPreference(activity: AppCompatActivity) {
        val editor = sharedPreference()?.edit()
        editor?.putString("LAST_ACTIVITY", activity::class.java.name)
        editor?.apply()
    }

    fun readActivityFromPreference(): String {
        return sharedPreference()?.getString("LAST_ACTIVITY", "") ?: ""
    }

}
