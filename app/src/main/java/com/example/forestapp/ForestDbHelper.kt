package com.example.forestapp

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class ForestDbHelper(context: Context) : SQLiteOpenHelper(
    context,
    DATABASE_NAME,
    null,
    DATABASE_VERSION
) {
    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "ForestApp.db"

        private const val SQL_CREATE_USERS_TABLE = """
            CREATE TABLE ${DatabaseContract.UserTable.TABLE_NAME} (
                ${DatabaseContract.UserTable.COLUMN_ID} INTEGER PRIMARY KEY AUTOINCREMENT,
                ${DatabaseContract.UserTable.COLUMN_NAME} TEXT,
                ${DatabaseContract.UserTable.COLUMN_USERNAME} TEXT UNIQUE,
                ${DatabaseContract.UserTable.COLUMN_EMAIL} TEXT UNIQUE,
                ${DatabaseContract.UserTable.COLUMN_COINS} INTEGER,
                ${DatabaseContract.UserTable.COLUMN_TOTAL_FOCUS_TIME} INTEGER,
                ${DatabaseContract.UserTable.COLUMN_TREES_PLANTED} INTEGER,
                ${DatabaseContract.UserTable.COLUMN_REAL_TREES_PLANTED} INTEGER,
                ${DatabaseContract.UserTable.COLUMN_DAILY_GOAL} INTEGER DEFAULT 25
            )
        """

        private const val SQL_CREATE_SESSIONS_TABLE = """
            CREATE TABLE ${DatabaseContract.SessionTable.TABLE_NAME} (
                ${DatabaseContract.SessionTable.COLUMN_ID} INTEGER PRIMARY KEY AUTOINCREMENT,
                ${DatabaseContract.SessionTable.COLUMN_DURATION} INTEGER,
                ${DatabaseContract.SessionTable.COLUMN_TREE_TYPE} TEXT,
                ${DatabaseContract.SessionTable.COLUMN_DATE} INTEGER,
                ${DatabaseContract.SessionTable.COLUMN_SUCCESSFUL} INTEGER,
                ${DatabaseContract.SessionTable.COLUMN_USER_ID} INTEGER DEFAULT 1
            )
        """

        private const val SQL_CREATE_TREES_TABLE = """
            CREATE TABLE ${DatabaseContract.TreeTable.TABLE_NAME} (
                ${DatabaseContract.TreeTable.COLUMN_ID} INTEGER PRIMARY KEY AUTOINCREMENT,
                ${DatabaseContract.TreeTable.COLUMN_TYPE} TEXT,
                ${DatabaseContract.TreeTable.COLUMN_PLANT_DATE} INTEGER,
                ${DatabaseContract.TreeTable.COLUMN_DAYS_GROWN} INTEGER,
                ${DatabaseContract.TreeTable.COLUMN_IS_REAL_TREE} INTEGER DEFAULT 0,
                ${DatabaseContract.TreeTable.COLUMN_USER_ID} INTEGER DEFAULT 1
            )
        """
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_USERS_TABLE)
        db.execSQL(SQL_CREATE_SESSIONS_TABLE)
        db.execSQL(SQL_CREATE_TREES_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS ${DatabaseContract.UserTable.TABLE_NAME}")
        db.execSQL("DROP TABLE IF EXISTS ${DatabaseContract.SessionTable.TABLE_NAME}")
        db.execSQL("DROP TABLE IF EXISTS ${DatabaseContract.TreeTable.TABLE_NAME}")
        onCreate(db)
    }
}