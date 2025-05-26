package com.example.forestapp

import android.content.ContentValues
import android.content.Context
import java.util.Date

class SessionRepository(context: Context) {
    private val dbHelper = ForestDbHelper(context)

    fun insertSession(session: Session): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DatabaseContract.SessionTable.COLUMN_DURATION, session.duration)
            put(DatabaseContract.SessionTable.COLUMN_TREE_TYPE, session.treeType)
            put(DatabaseContract.SessionTable.COLUMN_DATE, session.date.time)
            put(DatabaseContract.SessionTable.COLUMN_SUCCESSFUL, if (session.successful) 1 else 0)
            put(DatabaseContract.SessionTable.COLUMN_USER_ID, session.userId)
        }
        return db.insert(DatabaseContract.SessionTable.TABLE_NAME, null, values)
    }

    fun getSessionsForUser(userId: Int): List<Session> {
        val db = dbHelper.readableDatabase
        val sessions = mutableListOf<Session>()
        val cursor = db.query(
            DatabaseContract.SessionTable.TABLE_NAME,
            null,
            "${DatabaseContract.SessionTable.COLUMN_USER_ID} = ?",
            arrayOf(userId.toString()),
            null,
            null,
            "${DatabaseContract.SessionTable.COLUMN_DATE} DESC"
        )

        while (cursor.moveToNext()) {
            val session = Session(
                id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.SessionTable.COLUMN_ID)),
                duration = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.SessionTable.COLUMN_DURATION)),
                treeType = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.SessionTable.COLUMN_TREE_TYPE)),
                date = Date(cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseContract.SessionTable.COLUMN_DATE))),
                successful = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.SessionTable.COLUMN_SUCCESSFUL)) == 1,
                userId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.SessionTable.COLUMN_USER_ID))
            )
            sessions.add(session)
        }

        cursor.close()
        return sessions
    }
}
