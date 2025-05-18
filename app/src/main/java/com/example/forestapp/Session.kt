package com.example.forestapp

import java.util.Date

data class Session(
    val id: Int = 0,
    val duration: Int,
    val treeType: String,
    val date: Date,
    val successful: Boolean,
    val userId: Int = 1
)
