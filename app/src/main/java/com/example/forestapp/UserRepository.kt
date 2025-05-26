package com.example.forestapp

import android.content.Context
import com.example.forestapp.util.SharedPreferencesUtils

class UserRepository(private val context: Context) {

    private val dbHelper = ForestDbHelper(context)

    fun getUser(): User? {
        val userId = SharedPreferencesUtils.getUserId(context)
        if (userId == -1) return null

        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM users WHERE id = ?", arrayOf(userId.toString()))

        val user: User? = if (cursor.moveToFirst()) {
            User(
                id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                name = cursor.getString(cursor.getColumnIndexOrThrow("name")),
                username = cursor.getString(cursor.getColumnIndexOrThrow("username")),
                email = cursor.getString(cursor.getColumnIndexOrThrow("email")),
                password = cursor.getString(cursor.getColumnIndexOrThrow("password")),
                coins = cursor.getInt(cursor.getColumnIndexOrThrow("coins")),
                totalFocusTime = cursor.getInt(cursor.getColumnIndexOrThrow("total_focus_time")),
                treesPlanted = cursor.getInt(cursor.getColumnIndexOrThrow("trees_planted")),
                realTreesPlanted = cursor.getInt(cursor.getColumnIndexOrThrow("real_trees_planted")),
                dailyGoal = cursor.getInt(cursor.getColumnIndexOrThrow("daily_goal"))
            )
        } else null

        cursor.close()
        return user
    }

    fun addCoins(amount: Int) {
        val user = getUser() ?: return
        val db = dbHelper.writableDatabase
        val newCoins = user.coins + amount
        db.execSQL("UPDATE users SET coins = ? WHERE id = ?", arrayOf(newCoins, user.id))
    }

    fun updateFocusTime(minutes: Int) {
        val user = getUser() ?: return
        val db = dbHelper.writableDatabase
        val newTime = user.totalFocusTime + minutes
        db.execSQL("UPDATE users SET total_focus_time = ? WHERE id = ?", arrayOf(newTime, user.id))
    }
}
