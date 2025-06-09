package com.example.forestapp.model

import java.util.Date

data class Tree(
    val id: String = "",
    val type: String = "",
    val plantDate: Date = Date(),
    val daysGrown: Int = 0,
    val isRealTree: Boolean = false,
    val userId: String = ""
)