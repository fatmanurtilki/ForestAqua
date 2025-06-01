package com.example.forestapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.forestapp.databinding.ActivityShopBinding
import com.example.forestapp.util.SharedPreferencesUtils
import com.google.android.material.bottomnavigation.BottomNavigationView
import ui.fragments.ForestFragment

class ShopActivity : AppCompatActivity(), ShopAdapter.OnPurchaseListener {

    private lateinit var binding: ActivityShopBinding
    private lateinit var user: User
    private lateinit var shopAdapter: ShopAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShopBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userId = SharedPreferencesUtils.getUserId(this)
        user = UserRepository(this).getUserById(userId) ?: return

        setupUI()
        updateCoinDisplay()
        setupBottomNavigation()
    }

    private fun setupBottomNavigation() {
        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)

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

    private fun setupUI() {
        shopAdapter = ShopAdapter(
            treeList = TreeType.getAllTypes(),
            user = user,
            treeDrawables = TreeType.treeDrawables,
            context = this,
            onPurchaseListener = this
        )

        binding.recyclerViewShop.apply {
            layoutManager = LinearLayoutManager(this@ShopActivity)
            adapter = shopAdapter
        }
    }

    override fun onTreePurchased(treeName: String, price: Int) {
        user.coins -= price
        UserRepository(this).updateUser(user)  // coin güncellemesini veri tabanına da işle
        updateCoinDisplay()
        shopAdapter.notifyDataSetChanged()
        Toast.makeText(this, "$treeName satın alındı! Kalan coin: ${user.coins}", Toast.LENGTH_SHORT).show()
    }

    private fun updateCoinDisplay() {
        binding.tvCoinCount.text = "${user.coins} Coin"
    }
}