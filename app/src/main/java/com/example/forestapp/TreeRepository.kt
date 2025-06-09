package com.example.forestapp.repository

import com.example.forestapp.model.Tree
import com.google.firebase.firestore.FirebaseFirestore

class TreeRepository {

    private val db = FirebaseFirestore.getInstance()
    private val treeCollection = db.collection("trees")

    fun insertTree(tree: Tree, onSuccess: () -> Unit = {}, onFailure: (Exception) -> Unit = {}) {
        treeCollection.add(tree)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }

    fun getTreesByUser(userId: String, onResult: (List<Tree>) -> Unit) {
        treeCollection.whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { snapshot ->
                val trees = snapshot.toObjects(Tree::class.java)
                onResult(trees)
            }
            .addOnFailureListener {
                onResult(emptyList())
            }
    }
}