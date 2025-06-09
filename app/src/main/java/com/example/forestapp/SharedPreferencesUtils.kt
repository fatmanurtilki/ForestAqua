package com.example.forestapp.util

import android.content.Context

object SharedPreferencesUtils {
    private const val PREFS_NAME = "ForestPrefs"
    private const val KEY_LOGGED_IN = "isLoggedIn"
    private const val KEY_USER_ID = "userId"

    fun setLoggedIn(context: Context, loggedIn: Boolean) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit().putBoolean(KEY_LOGGED_IN, loggedIn).apply()
    }

    fun isLoggedIn(context: Context): Boolean {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getBoolean(KEY_LOGGED_IN, false)
    }

    fun setUserId(context: Context, userId: String) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit().putString(KEY_USER_ID, userId).apply()
    }

    fun getUserId(context: Context): String {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getString(KEY_USER_ID, "") ?: ""
    }

    fun clearPreferences(context: Context) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit().clear().apply()
    }
}