package com.example.forestapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.forestapp.util.SharedPreferencesUtils
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth

class SplashActivity : AppCompatActivity() {
    private val auth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        setContentView(R.layout.activity_splash)

        Handler(Looper.getMainLooper()).postDelayed({
            val user = auth.currentUser
            if (user != null) {
                SharedPreferencesUtils.setLoggedIn(this, true)
                SharedPreferencesUtils.setUserId(this, user.uid)
                startActivity(Intent(this, MainActivity::class.java))
            } else {
                startActivity(Intent(this, LoginActivity::class.java))
            }
            finish()
        }, 2000)
    }

    override fun attachBaseContext(newBase: Context) {
        val lang = SharedPreferencesUtils.getAppLanguage(newBase)
        super.attachBaseContext(com.example.forestapp.util.LocaleHelper.setLocale(newBase, lang))
    }
}