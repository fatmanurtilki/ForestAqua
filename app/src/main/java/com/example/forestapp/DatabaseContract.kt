package com.example.forestapp

object DatabaseContract {
    object UserTable {
        const val TABLE_NAME = "users"
        const val COLUMN_USERNAME = "username"
        const val COLUMN_EMAIL = "email"
        const val COLUMN_ID = "id"
        const val COLUMN_NAME = "name"
        const val COLUMN_COINS = "coins"
        const val COLUMN_TOTAL_FOCUS_TIME = "total_focus_time"
        const val COLUMN_TREES_PLANTED = "trees_planted"
        const val COLUMN_REAL_TREES_PLANTED = "real_trees_planted"
        const val COLUMN_DAILY_GOAL = "daily_goal"

    }

    object SessionTable {
        const val TABLE_NAME = "sessions"
        const val COLUMN_ID = "id"
        const val COLUMN_DURATION = "duration"
        const val COLUMN_TREE_TYPE = "tree_type"
        const val COLUMN_DATE = "date"
        const val COLUMN_SUCCESSFUL = "successful"
        const val COLUMN_USER_ID = "user_id"
    }

    object TreeTable {
        const val TABLE_NAME = "trees"
        const val COLUMN_ID = "id"
        const val COLUMN_TYPE = "type"
        const val COLUMN_PLANT_DATE = "plant_date"
        const val COLUMN_DAYS_GROWN = "days_grown"
        const val COLUMN_IS_REAL_TREE = "is_real_tree"
        const val COLUMN_USER_ID = "user_id"
    }
}