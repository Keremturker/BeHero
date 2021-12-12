package com.keremturker.behero.utils


import android.content.Context
import android.content.SharedPreferences
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import com.google.gson.Gson
import com.google.gson.JsonParser
import com.keremturker.behero.model.Users
import com.keremturker.behero.utils.Constants.emptyText
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedHelper @Inject constructor(@ApplicationContext private val context: Context) {

    companion object {
        private const val sharedPrefKey = "sharedPrefKey"
        private const val PREFERENCES_USERS = "preferences_users"
    }


    private val sharedPref: SharedPreferences by lazy {
        context.getSharedPreferences(
            sharedPrefKey,
            Context.MODE_PRIVATE
        )
    }

    private val gson: Gson by lazy { Gson() }

    private fun saveBoolean(key: String, value: Boolean) {
        sharedPref.edit().apply {
            putBoolean(key, value)
            apply()
        }
    }

    private fun saveInt(key: String, value: Int) {
        sharedPref.edit().apply {
            putInt(key, value)
            apply()
        }
    }

    private fun saveString(key: String, value: String) {
        sharedPref.edit().apply {
            putString(key, value)
            apply()
        }
    }


    private fun saveBooleanData(key: String, value: Boolean) {
        sharedPref.edit().apply {
            putBoolean(key, value)
            apply()
        }
    }


    private fun <T> saveList(@NonNull key: String, list: List<T>) {
        putStringData(key, gson.toJsonTree(list).asJsonArray.toString())
        sharedPref.edit().apply()
    }


    private fun <T> loadList(key: String, clazz: Class<T>): List<T>? {
        return try {
            // Consuming remote method
            val strJson = sharedPref.getString(key, null)
            val parser = JsonParser()
            val array = parser.parse(strJson).asJsonArray
            val mutableList: MutableList<T> = ArrayList()
            for (json in array) {
                val entity: T = Gson().fromJson(json, clazz)
                mutableList.add(entity)
            }
            mutableList
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            listOf()
        }
    }


    /**
     * Helps save String data
     */
    private fun putStringData(@NonNull key: String, @Nullable value: String) {
        sharedPref.edit().putString(key, value).apply()
    }

    /**
     * Helps save String data
     */
    private fun removeData(@NonNull key: String) {
        sharedPref.edit().remove(key).apply()
    }

    /**
     * Helps get String data
     */
    private fun getStringData(@NonNull key: String, @Nullable defVal: String?): String? {
        return sharedPref.getString(key, defVal)
    }

    /**
     * Helps get String data
     */
    private fun getBooleanData(@NonNull key: String): Boolean {
        return sharedPref.getBoolean(key, false)
    }

    /**
     * Add object data with given key to Shared preferences
     */
    fun <T> addData(key: String, data: T?) {
        if (data == null) {
            putStringData(key, emptyText())
        } else {
            val json = gson.toJson(data)
            if (json != null) {
                putStringData(key, json)
            } else {
                throw RuntimeException("Data can not be null")
            }
        }
    }

    /**
     * Retrieve Object model with given key from Shared Preferences
     */
    @Nullable
    private fun <T> retrieveData(key: String, clazz: Class<T>): T? {
        return try {
            if (sharedPref.contains(key)) {
                val json = getStringData(key, null) ?: return null
                if (json.isBlank()) {
                    return null
                }
                return gson.fromJson(json, clazz)
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Diskteki tüm sharedPref data'sini siler
     */
    fun clearAllData() {
        sharedPref.edit().clear().apply()
    }


    private fun putLongArrayList(key: String, list: ArrayList<Long>?) {
        sharedPref.edit().apply {
            putString(key, list?.joinToString(",") ?: emptyText())
            apply()
        }
    }

    private fun getLongArrayList(key: String): ArrayList<Long> {
        val value = sharedPref.getString(key, null)
        return if (value.isNullOrBlank()) {
            arrayListOf()
        } else {
            ArrayList(value.split(",").map { it.toLong() })
        }
    }


    var syncUsers: Users?
        get() = retrieveData(PREFERENCES_USERS, Users::class.java)
        set(value) = addData(PREFERENCES_USERS, value)
}