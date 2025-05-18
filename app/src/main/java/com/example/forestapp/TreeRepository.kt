package com.example.forestapp

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import java.util.Date

class TreeRepository(context: Context) {
    private val dbHelper = ForestDbHelper(context)

    fun insertTree(tree: Tree): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DatabaseContract.TreeTable.COLUMN_TYPE, tree.type)
            put(DatabaseContract.TreeTable.COLUMN_PLANT_DATE, tree.plantDate.time)
            put(DatabaseContract.TreeTable.COLUMN_DAYS_GROWN, tree.daysGrown)
            put(DatabaseContract.TreeTable.COLUMN_IS_REAL_TREE, if (tree.isRealTree) 1 else 0)
            put(DatabaseContract.TreeTable.COLUMN_USER_ID, tree.userId)
        }
        return db.insert(DatabaseContract.TreeTable.TABLE_NAME, null, values)
    }

    fun getTrees(): List<Tree> {
        val db = dbHelper.readableDatabase
        val trees = mutableListOf<Tree>()
        val cursor = db.query(
            DatabaseContract.TreeTable.TABLE_NAME,
            null,
            null, null, null, null,
            "${DatabaseContract.TreeTable.COLUMN_PLANT_DATE} DESC"
        )

        while (cursor.moveToNext()) {
            trees.add(parseTree(cursor))
        }
        cursor.close()
        return trees
    }

    private fun parseTree(cursor: Cursor): Tree {
        return Tree(
            id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.TreeTable.COLUMN_ID)),
            type = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.TreeTable.COLUMN_TYPE)),
            plantDate = Date(cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseContract.TreeTable.COLUMN_PLANT_DATE))),
            daysGrown = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.TreeTable.COLUMN_DAYS_GROWN)),
            isRealTree = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.TreeTable.COLUMN_IS_REAL_TREE)) == 1,
            userId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.TreeTable.COLUMN_USER_ID))
        )
    }
}