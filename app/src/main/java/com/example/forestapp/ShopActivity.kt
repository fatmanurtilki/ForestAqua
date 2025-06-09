package com.example.forestapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.forestapp.databinding.ActivityShopBinding
import com.example.forestapp.model.Tree
import com.example.forestapp.repository.TreeRepository
import com.example.forestapp.repository.UserRepository
import com.example.forestapp.util.SharedPreferencesUtils
import com.google.android.material.bottomnavigation.BottomNavigationView

class ShopActivity : AppCompatActivity(), ShopAdapter.OnPurchaseListener {

    private lateinit var binding: ActivityShopBinding
    private val userRepo = UserRepository()
    private val treeRepo = TreeRepository()
    private lateinit var userId: String
    private var currentCoin: Int = 0

    private lateinit var shopAdapter: ShopAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShopBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userId = SharedPreferencesUtils.getUserId(this)
        loadUserAndSetupUI()
        setupBottomNavigation()
    }

    private fun loadUserAndSetupUI() {
        userRepo.getUserById(userId) { user ->
            if (user != null) {
                currentCoin = user.coins
                binding.tvCoinCount.text = "$currentCoin Coin"
                shopAdapter = ShopAdapter(TreeType.getAllTypes(), currentCoin, TreeType.treeDrawables, this, this)
                binding.recyclerViewShop.layoutManager = LinearLayoutManager(this)
                binding.recyclerViewShop.adapter = shopAdapter
            }
        }
    }

    override fun onTreePurchased(treeName: String, price: Int) {
        currentCoin -= price
        binding.tvCoinCount.text = "$currentCoin Coin"
        userRepo.addCoins(userId, -price)

        val tree = Tree(
            type = treeName,
            plantDate = java.util.Date(),
            daysGrown = 0,
            isRealTree = false,
            userId = userId
        )
        treeRepo.insertTree(tree)
        Toast.makeText(this, "$treeName satın alındı!", Toast.LENGTH_SHORT).show()
        shopAdapter.updateUserCoin(currentCoin)
    }

    private fun setupBottomNavigation() {
        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNavigation.selectedItemId = R.id.nav_forest
        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_timer -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                    true
                }
                R.id.nav_forest -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                    true
                }
                else -> false
            }
        }
    }
}