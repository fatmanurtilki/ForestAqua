package com.example.forestapp

import java.util.Date

data class Tree(
    val id: Int = 0,
    val type: String,
    val plantDate: Date,
    val daysGrown: Int,
    val isRealTree: Boolean = false,
    val userId: Int = 1
)
