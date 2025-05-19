package com.example.forestapp

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.forestapp.databinding.ActivityShopBinding

class ShopActivity : AppCompatActivity(), ShopAdapter.OnPurchaseListener {

    private lateinit var binding: ActivityShopBinding
    private lateinit var user: User
    private lateinit var shopAdapter: ShopAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShopBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Kullanıcı verilerini al (örneğin SharedPreferences veya database'den)
        user = getCurrentUser() // Bu metodu kendi veri kaynağınıza göre implement edin

        setupUI()
        updateCoinDisplay()
    }

    private fun getCurrentUser(): User {
        // Örnek implementasyon - gerçek uygulamada veritabanından veya SharedPreferences'tan alın
        return User(
            id = 1,
            name = "Test Kullanıcı",
            coins = 150, // Başlangıç coin miktarı
            totalFocusTime = 1200,
            treesPlanted = 35,
            dailyGoal = 25
        )
    }

    private fun setupUI() {
        shopAdapter = ShopAdapter(
            treeList = TreeType.getAllTypes(),
            user = user, // Tüm kullanıcı nesnesini iletiyoruz
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
        // Coin'leri güncelle
        user.coins -= price

        // Veritabanını/SharedPreferences'ı güncelle
        updateUserData(user)

        // UI'ı güncelle
        updateCoinDisplay()
        shopAdapter.notifyDataSetChanged() // Tüm listeyi yenile

        Toast.makeText(this, "$treeName satın alındı! Kalan coin: ${user.coins}", Toast.LENGTH_SHORT).show()
    }

    private fun updateUserData(user: User) {
        // Burada kullanıcı verilerini kalıcı olarak kaydedin
        // Örneğin: SharedPreferences, Room Database veya Firebase
    }

    private fun updateCoinDisplay() {
        binding.tvCoinCount.text = "${user.coins} Coin"
    }
}