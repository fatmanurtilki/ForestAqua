package com.example.forestapp

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.forestapp.databinding.ActivityMainBinding
import com.example.forestapp.util.LocaleHelper
import com.example.forestapp.util.SharedPreferencesUtils
import ui.fragments.ForestFragment
import ui.fragments.TimerFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun attachBaseContext(newBase: Context?) {
        val lang = SharedPreferencesUtils.getAppLanguage(newBase!!)
        val context = LocaleHelper.setLocale(newBase, lang)
        super.attachBaseContext(context)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SharedPreferencesUtils.applySavedBackgroundColor(this)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupBottomNavigation()
        loadFragment(TimerFragment())
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_timer -> loadFragment(TimerFragment())
                R.id.nav_forest -> loadFragment(ForestFragment())
                else -> false
            }
            true
        }
    }

    private fun loadFragment(fragment: Fragment): Boolean {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
        return true
    }


}
