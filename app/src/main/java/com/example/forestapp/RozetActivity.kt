package com.example.forestapp

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.forestapp.databinding.ActivityRozetBinding
import com.example.forestapp.repository.UserRepository
import com.example.forestapp.util.SharedPreferencesUtils

class RozetActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRozetBinding
    private val userRepo = UserRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRozetBinding.inflate(layoutInflater)
        setContentView(binding.root)
        SharedPreferencesUtils.applySavedLanguage(this)
        SharedPreferencesUtils.applySavedBackgroundColor(this)

        val userId = SharedPreferencesUtils.getUserId(this)
        userRepo.getUserById(userId) { user ->
            user?.let {
                binding.tvCoinCount.text = "${it.coins} Coin"
                val earned = mutableListOf<String>()

                if (it.coins >= 10) earned.add(RozetType.DENIZATI)
                if (it.coins >= 20) earned.add(RozetType.YELKENLI)
                if (it.coins >= 25) earned.add(RozetType.DENIZKABUK)
                if (it.totalFocusTime >= 1) earned.add(RozetType.INCI)
                if (it.totalFocusTime >= 60) earned.add(RozetType.DENIZYILDIZI)
                if (it.totalFocusTime >= 100) earned.add(RozetType.KAPLUMBAGA)

                val adapter = RozetAdapter(RozetType.getAllRozetTypes(), earned, RozetType.rozetDrawables)
                binding.recyclerViewRozets.layoutManager = LinearLayoutManager(this)
                binding.recyclerViewRozets.adapter = adapter

                if (earned.isNotEmpty()) {
                    Toast.makeText(this, "Yeni rozet(ler) kazandınız!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}