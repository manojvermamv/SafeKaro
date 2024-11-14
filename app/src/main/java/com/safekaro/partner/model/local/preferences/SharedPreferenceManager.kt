package com.safekaro.partner.model.local.preferences

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.safekaro.partner.model.models.UserData
import java.lang.reflect.Type

class SharedPreferenceManager constructor(private val context: Context) {

    val pref: SharedPreferences = context.getSharedPreferences()
    private val editor = pref.edit()
    private val gson = Gson()

    private inline fun <reified T> read(param: PrefParam, defValue: T? = null): T {
        val prefKey = param.key()
        @Suppress("UNCHECKED_CAST")
        val value: Any? = when (defValue) {
            is String -> pref.getString(prefKey, defValue as? String ?: "")
            is Boolean -> pref.getBoolean(prefKey, (defValue as? Any ?: false) as Boolean)
            is Long -> pref.getLong(prefKey, defValue as? Long ?: 0)
            is Int -> pref.getInt(prefKey, defValue as? Int ?: 0)
            is Float -> pref.getFloat(prefKey, defValue as? Float ?: 0f)
            is Set<*> -> pref.getStringSet(prefKey, defValue as? Set<String> ?: setOf<String>())
            is MutableSet<*> -> pref.getStringSet(prefKey, defValue as? MutableSet<String> ?: mutableSetOf<String>())
            else -> {
                val json = pref.getString(prefKey, null)
                if (!json.isNullOrEmpty()) {
                    gson.fromJson(json, object : TypeToken<T>() {}.type)
                    //gson.fromJson(json, object : TypeToken<ArrayList<T>>() {}.type)
                } else defValue
            }
        }
        return (value as T)
    }

    private fun write(param: PrefParam, value: Any, type: Type = value.javaClass) {
        val prefKey = param.key()
        @Suppress("UNCHECKED_CAST")
        when (value) {
            is String -> editor.putString(prefKey, value)
            is Boolean -> editor.putBoolean(prefKey, value)
            is Long -> editor.putLong(prefKey, value)
            is Int -> editor.putInt(prefKey, value)
            is Float -> editor.putFloat(prefKey, value)
            is Set<*> -> editor.putStringSet(prefKey, value as Set<String>)
            is MutableSet<*> -> editor.putStringSet(prefKey, value as MutableSet<String>)
            else -> editor.putString(prefKey, gson.toJson(value, type))
        }.apply()
    }

    inline fun <reified T> liveData(param: PrefParam, default: T) = pref.liveData(param.key(), default)

    fun clear() {
        try {
            pref.edit().clear().apply()
        } catch (e1: SecurityException) {
            Log.wtf("SharedPreferenceManager", e1.message ?: e1.toString())
            try {
                context.getSharedPreferences().edit().clear().apply()
            } catch (e2: SecurityException) {
                Log.wtf("SharedPreferenceManager", e2.message ?: e2.toString())
            }
        }
    }

    fun lastRefreshTime(value: Long) = write(PrefParam.PREF_LAST_REFRESH_TIME, value)
    fun lastRefreshTime(): Long = read(PrefParam.PREF_LAST_REFRESH_TIME, 0L)

    fun welcomeScreenSkip(value: Boolean) = write(PrefParam.WELCOME_SCREEN_SKIP, value)
    fun welcomeScreenSkip(): Boolean = read(PrefParam.WELCOME_SCREEN_SKIP, false)

    fun isNewRegistration(value: Boolean) = write(PrefParam.IS_NEW_REGISTRATION, value)
    fun isNewRegistration(): Boolean = read(PrefParam.IS_NEW_REGISTRATION, false)

    fun loggedUID(value: String) = write(PrefParam.LOGGED_IN_UID, value) // this is a uid of backend db (aka, local_id)
    fun loggedUID(): String = read(PrefParam.LOGGED_IN_UID, "")

    fun isLoggedIn(): Boolean = read(PrefParam.LOGGED_IN_UID, "").trim().isNotEmpty()

    fun userEmail(value: String) = write(PrefParam.USER_EMAIL, value)
    fun userEmail(): String = read(PrefParam.USER_EMAIL, "")

    fun userPassword(value: String) = write(PrefParam.USER_PASSWORD, value)
    fun userPassword(): String = read(PrefParam.USER_PASSWORD, "")

    // app settings
    fun userData(value: UserData) = write(PrefParam.USER_DATA, value, UserData::class.java)
    fun userData(): UserData = read(PrefParam.USER_DATA, UserData("", ""))

}

/**
 * shared preferences for global use
 * */
enum class PrefParam {
    PREF_LAST_REFRESH_TIME,
    WELCOME_SCREEN_SKIP,
    IS_NEW_REGISTRATION,
    LOGGED_IN_UID,
    USER_EMAIL,
    USER_PASSWORD,

    // app settings keys
    USER_DATA;

    fun key() = name.lowercase().trim()
}

private fun Context.getSharedPreferences(): SharedPreferences {
    val prefName = packageName + "_preferences"
    return getSharedPreferences(prefName, 0)
}