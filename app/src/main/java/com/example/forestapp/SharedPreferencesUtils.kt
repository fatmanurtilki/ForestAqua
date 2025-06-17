package com.example.forestapp.util

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.example.forestapp.R

object SharedPreferencesUtils {
    private const val PREFS_NAME = "ForestPrefs"
    private const val SETTINGS_PREFS = "SettingsPrefs"
    private const val KEY_LOGGED_IN = "isLoggedIn"
    private const val KEY_USER_ID = "userId"
    private const val KEY_APP_LANGUAGE = "app_language"
    private const val KEY_BACKGROUND_COLOR = "background_color"

    fun setLoggedIn(context: Context, loggedIn: Boolean) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(KEY_LOGGED_IN, loggedIn).apply()
    }

    fun isLoggedIn(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_LOGGED_IN, false)
    }

    fun setUserId(context: Context, userId: Int) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putInt(KEY_USER_ID, userId).apply()
    }

    fun getUserId(context: Context): Int {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getInt(KEY_USER_ID, -1)
    }

    fun clearPreferences(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().clear().apply()
    }
<<<<<<< Updated upstream
}
=======

    fun setAppLanguage(context: Context, langCode: String) {
        val prefs = context.getSharedPreferences(SETTINGS_PREFS, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_APP_LANGUAGE, langCode).apply()
    }

    fun getAppLanguage(context: Context): String {
        val prefs = context.getSharedPreferences(SETTINGS_PREFS, Context.MODE_PRIVATE)
        return prefs.getString(KEY_APP_LANGUAGE, "tr") ?: "tr"
    }

    fun applySavedLanguage(activity: AppCompatActivity) {
        val lang = getAppLanguage(activity)
        LocaleHelper.setLocale(activity, lang)
    }

    fun setBackgroundColor(context: Context, colorName: String) {
        val prefs = context.getSharedPreferences(SETTINGS_PREFS, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_BACKGROUND_COLOR, colorName).apply()
    }

    fun getBackgroundColor(context: Context): String {
        val prefs = context.getSharedPreferences(SETTINGS_PREFS, Context.MODE_PRIVATE)
        return prefs.getString(KEY_BACKGROUND_COLOR, "gereken_mavi") ?: "gereken_mavi"
    }

    fun applySavedBackgroundColor(activity: AppCompatActivity) {
        val colorName = getBackgroundColor(activity)
        val colorRes = when (colorName) {
            "gereken_mavi" -> R.color.gereken_mavi
            "gereken_sari" -> R.color.gereken_sari
            "gereken_pembe" -> R.color.gereken_pembe
            else -> R.color.gereken_mavi
        }
        activity.window.decorView.setBackgroundColor(activity.getColor(colorRes))
    }
}
>>>>>>> Stashed changes
