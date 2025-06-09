package com.example.forestapp.model

data class User(
    val id: String = "",
    val name: String = "",
    val username: String = "",
    val email: String = "",
    val password: String = "",
    var coins: Int = 0,
    var totalFocusTime: Int = 0,
    var treesPlanted: Int = 0,
    var realTreesPlanted: Int = 0,
    var dailyGoal: Int = 25
)