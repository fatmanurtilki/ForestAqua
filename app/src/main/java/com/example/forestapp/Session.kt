package com.example.forestapp.model

import java.util.Date

data class Session(
    val id: String = "",
    val duration: Int = 0,
    val treeType: String = "",
    val date: Date = Date(),
    val successful: Boolean = false,
    val userId: String = ""
)