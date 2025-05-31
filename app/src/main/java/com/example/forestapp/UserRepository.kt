package com.example.forestapp

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import kotlin.String

class UserRepository(context: Context) {
    private val dbHelper = ForestDbHelper(context)

    fun getUser(): User? {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            DatabaseContract.UserTable.TABLE_NAME,
            null,
            "${DatabaseContract.UserTable.COLUMN_ID} = ?",
            arrayOf("1"),
            null, null, null
        )

        return if (cursor.moveToFirst()) {
            parseUser(cursor)
        } else {
            null
        }.also { cursor.close() }
    }

    fun createInitialUser(): Long {
        val db = dbHelper.writableDatabase
        val user = User(
            name = "Forest User",
            username = "defaultuser",
            email = "default@forest.com",
            password = "1234",
            coins = 0,
            totalFocusTime = 0,
            treesPlanted = 0,
            realTreesPlanted = 0,
            dailyGoal = 25
        )

        return insertUser(db, user)
    }

    fun addCoins(amount: Int): Int {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DatabaseContract.UserTable.COLUMN_COINS, amount)
        }
        return db.update(
            DatabaseContract.UserTable.TABLE_NAME,
            values,
            "${DatabaseContract.UserTable.COLUMN_ID} = 1",
            null
        )
    }

    private fun insertUser(db: SQLiteDatabase, user: User): Long {
        val values = ContentValues().apply {
            put(DatabaseContract.UserTable.COLUMN_NAME, user.name)
            put(DatabaseContract.UserTable.COLUMN_COINS, user.coins)
            put(DatabaseContract.UserTable.COLUMN_TOTAL_FOCUS_TIME, user.totalFocusTime)
            put(DatabaseContract.UserTable.COLUMN_TREES_PLANTED, user.treesPlanted)
            put(DatabaseContract.UserTable.COLUMN_DAILY_GOAL, user.dailyGoal)
        }
        return db.insert(DatabaseContract.UserTable.TABLE_NAME, null, values)
    }

    private fun parseUser(cursor: Cursor): User {
        return User(
            id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.UserTable.COLUMN_ID)),
            name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.UserTable.COLUMN_NAME)),
            username = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.UserTable.COLUMN_USERNAME)),
            email = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.UserTable.COLUMN_EMAIL)),
            password = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.UserTable.COLUMN_PASSWORD)),
            coins = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.UserTable.COLUMN_COINS)),
            totalFocusTime = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.UserTable.COLUMN_TOTAL_FOCUS_TIME)),
            treesPlanted = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.UserTable.COLUMN_TREES_PLANTED)),
            realTreesPlanted = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.UserTable.COLUMN_REAL_TREES_PLANTED)),
            dailyGoal = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.UserTable.COLUMN_DAILY_GOAL))
        )
    }
    fun addCoinsForUser(userId: Int, coinsToAdd: Int) {
        val user = getUserById(userId) ?: return
        val newTotal = user.coins + coinsToAdd

        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DatabaseContract.UserTable.COLUMN_COINS, newTotal)
        }
        db.update(
            DatabaseContract.UserTable.TABLE_NAME,
            values,
            "${DatabaseContract.UserTable.COLUMN_ID} = ?",
            arrayOf(userId.toString())
        )
    }

    fun addFocusTime(userId: Int, minutes: Int) {
        val user = getUserById(userId) ?: return
        val newTotal = user.totalFocusTime + minutes

        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DatabaseContract.UserTable.COLUMN_TOTAL_FOCUS_TIME, newTotal)
        }
        db.update(
            DatabaseContract.UserTable.TABLE_NAME,
            values,
            "${DatabaseContract.UserTable.COLUMN_ID} = ?",
            arrayOf(userId.toString())
        )
    }

    fun getUserById(userId: Int): User? {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            DatabaseContract.UserTable.TABLE_NAME,
            null,
            "${DatabaseContract.UserTable.COLUMN_ID} = ?",
            arrayOf(userId.toString()),
            null, null, null
        )

        return if (cursor.moveToFirst()) parseUser(cursor) else null
            .also { cursor.close() }
    }
    fun updateUser(user: User) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DatabaseContract.UserTable.COLUMN_NAME, user.name)
            put(DatabaseContract.UserTable.COLUMN_USERNAME, user.username)
            put(DatabaseContract.UserTable.COLUMN_EMAIL, user.email)
            put(DatabaseContract.UserTable.COLUMN_PASSWORD, user.password)
            put(DatabaseContract.UserTable.COLUMN_COINS, user.coins)
            put(DatabaseContract.UserTable.COLUMN_TOTAL_FOCUS_TIME, user.totalFocusTime)
            put(DatabaseContract.UserTable.COLUMN_TREES_PLANTED, user.treesPlanted)
            put(DatabaseContract.UserTable.COLUMN_REAL_TREES_PLANTED, user.realTreesPlanted)
            put(DatabaseContract.UserTable.COLUMN_DAILY_GOAL, user.dailyGoal)
        }
        db.update(
            DatabaseContract.UserTable.TABLE_NAME,
            values,
            "${DatabaseContract.UserTable.COLUMN_ID} = ?",
            arrayOf(user.id.toString())
        )
        db.close()
    }



}