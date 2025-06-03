package com.example.forestapp

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.forestapp.databinding.ActivityRozetBinding
import com.example.forestapp.util.SharedPreferencesUtils
import com.google.android.material.bottomnavigation.BottomNavigationView
import ui.fragments.ForestFragment
import java.text.SimpleDateFormat
import java.util.*

class RozetActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRozetBinding
    private lateinit var user: User
    private lateinit var sessionRepository: SessionRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRozetBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userId = SharedPreferencesUtils.getUserId(this)
        user = UserRepository(this).getUserById(userId) ?: return
        sessionRepository = SessionRepository(this)

        binding.tvCoinCount.text = "${user.coins} Coin"

        setupUI(userId)
        setupBottomNavigation()
    }

    private fun setupUI(userId: Int) {
        val earnedRozets = mutableListOf<String>()

        if (user.coins >= 10) earnedRozets.add(RozetType.DENIZATI)
        if (user.coins >= 20) earnedRozets.add(RozetType.YELKENLI)
        if (user.coins >= 25) earnedRozets.add(RozetType.DENIZKABUK)
        if (user.totalFocusTime > 0) earnedRozets.add(RozetType.INCI)
        if (checkConsecutiveDays(userId, 10, 2)) earnedRozets.add(RozetType.DENIZALTI)
        if (checkConsecutiveDays(userId, 25, 7)) earnedRozets.add(RozetType.DALGA)
        if (user.totalFocusTime >= 100) earnedRozets.add(RozetType.KAPLUMBAGA)
        if (checkSingleDayDuration(userId, 60)) earnedRozets.add(RozetType.DENIZYILDIZI)

        if (earnedRozets.isNotEmpty()) {
            Toast.makeText(this, "Yeni rozetler kazandın!", Toast.LENGTH_SHORT).show()
            AlertDialog.Builder(this)
                .setTitle("Tebrikler!")
                .setMessage("Yeni rozet(ler) kazandın: \n${earnedRozets.joinToString("\n")}")
                .setPositiveButton("Tamam", null)
                .show()
        }

        val rozetAdapter = RozetAdapter(
            RozetType.getAllRozetTypes(),
            earnedRozets,
            RozetType.rozetDrawables
        )

        binding.recyclerViewRozets.apply {
            layoutManager = LinearLayoutManager(this@RozetActivity)
            adapter = rozetAdapter
        }
    }

    private fun checkConsecutiveDays(userId: Int, minPerDay: Int, days: Int): Boolean {
        val sessions = sessionRepository.getSessionsForUser(userId)
        val dayDurations = mutableMapOf<String, Int>()

        sessions.forEach {
            val dateStr = android.text.format.DateFormat.format("yyyy-MM-dd", it.date).toString()
            dayDurations[dateStr] = (dayDurations[dateStr] ?: 0) + it.duration / 60
        }

        val sorted = dayDurations.toSortedMap()
        var count = 0
        var lastDate: Date? = null

        for ((dateStr, duration) in sorted) {
            val currentDate = SimpleDateFormat("yyyy-MM-dd").parse(dateStr) ?: continue
            if (duration >= minPerDay) {
                if (lastDate == null || (currentDate.time - lastDate.time).toInt() == 24 * 60 * 60 * 1000) {
                    count++
                    if (count >= days) return true
                } else {
                    count = 1
                }
                lastDate = currentDate
            }
        }

        return false
    }

    private fun checkSingleDayDuration(userId: Int, minDuration: Int): Boolean {
        val sessions = sessionRepository.getSessionsForUser(userId)
        val dayMap = mutableMapOf<String, Int>()
        sessions.forEach {
            val key = android.text.format.DateFormat.format("yyyy-MM-dd", it.date).toString()
            dayMap[key] = (dayMap[key] ?: 0) + it.duration / 60
        }
        return dayMap.any { it.value >= minDuration }
    }

    private fun setupBottomNavigation() {
        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigation.selectedItemId = R.id.nav_forest

        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_timer -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                    true
                }
                R.id.nav_forest -> {
                    startActivity(Intent(this, ForestFragment::class.java))
                    finish()
                    true
                }
                else -> false
            }
        }
    }
}
