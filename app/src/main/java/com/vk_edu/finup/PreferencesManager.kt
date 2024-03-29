package com.vk_edu.finup

import android.content.Context
import android.content.SharedPreferences

class PreferencesManager(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("FinUpPrefs", Context.MODE_PRIVATE)

    fun saveData(key: String, value: String) {
        val editor = sharedPreferences.edit()
        editor.putString(key, value)
        editor.apply()
    }


    fun getData(key: String, defaultValue: String): String {
        return sharedPreferences.getString(key, defaultValue) ?: defaultValue
    }
}

fun isLogged(data: String): Boolean {
    if (data == "true") {
        return true
    }
    return false
}

fun writeLogged(preferencesManager: PreferencesManager) {
    preferencesManager.saveData("logged", "true")
}

fun writeUnLogged(preferencesManager: PreferencesManager) {
    preferencesManager.saveData("logged", "false")
}