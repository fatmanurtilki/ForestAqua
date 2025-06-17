package com.example.forestapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.forestapp.util.SharedPreferencesUtils

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val isLoggedIn = SharedPreferencesUtils.isLoggedIn(this)

        Handler(Looper.getMainLooper()).postDelayed({
            if (isLoggedIn) {
                startActivity(Intent(this, MainActivity::class.java))
            } else {
                startActivity(Intent(this, LoginActivity::class.java))
            }
            finish()
        }, 2000)
    }
<<<<<<< Updated upstream
}
=======
    override fun attachBaseContext(newBase: Context) {
        val lang = SharedPreferencesUtils.getAppLanguage(newBase)
        super.attachBaseContext(com.example.forestapp.util.LocaleHelper.setLocale(newBase, lang))
    }
}
>>>>>>> Stashed changes
