package com.sa.nafhasehaprovider.common.sharedprefrence

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.sa.nafhasehaprovider.app.NafhasehaProviderApp
import com.sa.nafhasehaprovider.common.USER_TOKEN
import com.sa.nafhasehaprovider.entity.response.authenticationResponse.AuthResponse

class PreferencesUtils (context: Context) {

    private val gson: Gson = Gson()

    private val preferences: SharedPreferences = context.getSharedPreferences(
        "Provider_Nafhaseha",
        Context.MODE_PRIVATE
    )



    fun clearSharedPref(){
        preferences.edit().clear().apply()
    }


    fun putString(key: String, value: String) {
        preferences.edit().putString(key, value).apply()
    }

    fun deleteObject(name:String){
        preferences.edit().remove(name).commit()
    }
    fun getString(key: String, defValue: String): String? {
        return preferences.getString(key, defValue)
    }
    fun getInt(key: String, defValue: Int): Int? {
        return preferences.getInt(key, defValue)
    }

    fun putInt(key: String, value: Int) {
        preferences.edit().putInt(key, value).apply()
    }

    fun putObject(key: String, model: Any?) {
        if (model != null) {
            val serializedObject = gson.toJson(model)
            preferences.edit().putString(key, serializedObject).apply()
        } else {
            preferences.edit().remove(key).apply()
        }
    }

    fun saveUserData(activity: Activity, key: String, userData: AuthResponse) {
        SaveData(activity, key, userData)
    }

    fun SaveData(activity: Activity, data_Key: String, data_Value: Any) {
        preferences
        if (NafhasehaProviderApp.pref != null) {
            val editor: SharedPreferences.Editor =
                preferences.edit()
            val gson = Gson()
            val StringData = gson.toJson(data_Value)
            editor.putString(data_Key, StringData)
            editor.commit()
        }
    }

    fun loadUserData(activity: Context, key: String): AuthResponse? {
        preferences
        val userData: AuthResponse
        val gson = Gson()
        userData = gson.fromJson(
            LoadData(
                activity,
                key),AuthResponse::class.java)
        return userData
    }


    private fun LoadData(context: Context?, data_Key: String?): String? {
        preferences
        if (preferences != null) {
            val editor: SharedPreferences.Editor =
                preferences.edit()
        } else {
            preferences
        }
        return preferences.getString(
            data_Key,
            null
        )
    }

    fun <T> getObject(
        key: String,
        classType: Class<T>): T? {
        return if (preferences.contains(key)) {
            gson.fromJson(preferences.getString(key, ""), classType)
        } else null
    }

    var authToken: String? = preferences.getString(USER_TOKEN, "")
        set(value) {
            field = value
            value?.let { putString(USER_TOKEN, it) }
        }


//    var userData: String? = preferences.getString(USER_DATA, "")
//        set(value) {
//            field = value
//            value?.let { putString(USER_DATA,it) }
//        }


    fun putBoolean(key: String, value: Boolean) {
        val editor = preferences.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }
    fun getBoolean(key: String, defValue: Boolean): Boolean {
        return preferences.getBoolean(key, defValue)
    }

}