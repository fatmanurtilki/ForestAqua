package com.example.forestapp

data class User(
    val id: Int = 0,
    val name: String,
    var coins: Int,
    var totalFocusTime: Int,
    var treesPlanted: Int,
    var realTreesPlanted: Int,
    var dailyGoal: Int = 25
)
