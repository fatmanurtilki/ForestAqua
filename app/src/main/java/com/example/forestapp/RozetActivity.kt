package com.example.forestapp

<<<<<<< Updated upstream
import android.content.Intent
=======
import android.content.Context
>>>>>>> Stashed changes
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.forestapp.databinding.ActivityRozetBinding
<<<<<<< Updated upstream
=======
import com.example.forestapp.repository.UserRepository
import com.example.forestapp.util.LocaleHelper
>>>>>>> Stashed changes
import com.example.forestapp.util.SharedPreferencesUtils
import com.google.android.material.bottomnavigation.BottomNavigationView
import ui.fragments.ForestFragment

class RozetActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRozetBinding
    private lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRozetBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val userId = SharedPreferencesUtils.getUserId(this)
        user = UserRepository(this).getUserById(userId) ?: return

        binding.tvCoinCount.text = "${user.coins} Coin"

        setupUI()
        setupBottomNavigation()
    }

    private fun setupUI() {
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
        SharedPreferencesUtils.applySavedBackgroundColor(this)
        setContentView(R.layout.activity_rozet)
    }
    override fun attachBaseContext(newBase: Context?) {
        val lang = SharedPreferencesUtils.getAppLanguage(newBase!!)
        val context = LocaleHelper.setLocale(newBase, lang)
        super.attachBaseContext(context)
    }
}
