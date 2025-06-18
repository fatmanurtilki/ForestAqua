package com.example.forestapp.repository

import com.example.forestapp.model.User
import com.google.firebase.firestore.FirebaseFirestore

class UserRepository {
    private val db = FirebaseFirestore.getInstance()
    private val userCollection = db.collection("users")

    fun createUser(userId: String, user: User, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        userCollection.document(userId)
            .set(user)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }

    fun getUserById(userId: String, onResult: (User?) -> Unit) {
        userCollection.document(userId).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val user = document.toObject(User::class.java)
                    onResult(user)
                } else {
                    onResult(null)
                }
            }
            .addOnFailureListener {
                onResult(null)
            }
    }

    fun updateUser(userId: String, user: User) {
        userCollection.document(userId).set(user)
    }

    fun addCoins(userId: String, coinsToAdd: Int) {
        userCollection.document(userId).get().addOnSuccessListener {
            val user = it.toObject(User::class.java)
            user?.let {
                it.coins += coinsToAdd
                updateUser(userId, it)
            }
        }
    }

    fun addFocusTime(userId: String, minutes: Int) {
        userCollection.document(userId).get().addOnSuccessListener {
            val user = it.toObject(User::class.java)
            user?.let {
                it.totalFocusTime += minutes
                updateUser(userId, it)
            }
        }
    }

    // 🌱 YENİ: Her 25 dakikada bir ağaç (balık) artırma fonksiyonu
    fun incrementTreeCount(userId: String) {
        userCollection.document(userId).get().addOnSuccessListener {
            val user = it.toObject(User::class.java)
            user?.let {
                it.treesPlanted += 1
                updateUser(userId, it)
            }
        }
    }
}
