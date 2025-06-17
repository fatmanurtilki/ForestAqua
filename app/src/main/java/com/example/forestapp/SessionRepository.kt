package com.example.forestapp.repository

import com.example.forestapp.model.Session
import com.google.firebase.firestore.FirebaseFirestore

class SessionRepository {

    private val db = FirebaseFirestore.getInstance()
    private val sessionsRef = db.collection("sessions")

    fun insertSession(session: Session, onComplete: () -> Unit = {}) {
        sessionsRef.add(session)
            .addOnSuccessListener { onComplete() }
    }

    fun getSessionsForUser(userId: String, onResult: (List<Session>) -> Unit) {
        sessionsRef.whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { snapshot ->
                val sessions = snapshot.toObjects(Session::class.java)
                onResult(sessions)
            }
            .addOnFailureListener {
                onResult(emptyList())
            }
    }
}