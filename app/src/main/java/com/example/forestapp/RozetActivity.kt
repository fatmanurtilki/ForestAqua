package com.example.forestapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.forestapp.R
import com.example.forestapp.RozetType
import com.example.forestapp.RozetAdapter
import com.example.forestapp.databinding.ActivityRozetBinding
import com.example.forestapp.User
import com.example.forestapp.UserRepository
import com.example.forestapp.databinding.FragmentTimerBinding
import java.util.Date

class RozetActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRozetBinding
    private lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRozetBinding.inflate(layoutInflater)
        setContentView(binding.root)

        user = User(
            id = 1,
            name = "Test user",
            coins = 100,
            totalFocusTime = 1200,
            treesPlanted = 35,
            dailyGoal = 25
        )

        setupUI()
    }

    private fun setupUI() {

        // Rozetleri RecyclerView ile listele
        val rozetAdapter = RozetAdapter(
            RozetType.getAllRozetTypes(),
            RozetType.getEarnedRozets(user.treesPlanted),
            RozetType.rozetDrawables
        )

        binding.recyclerViewRozets.apply {
            layoutManager = LinearLayoutManager(this@RozetActivity)
            adapter = rozetAdapter
        }
    }
}