package com.example.forestapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.forestapp.repository.UserRepository
import com.example.forestapp.util.LocaleHelper
import com.example.forestapp.util.SharedPreferencesUtils
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private val userRepo = UserRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        val etEmail = findViewById<EditText>(R.id.etIdentifier)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnSignIn = findViewById<LinearLayout>(R.id.btnSignIn)
        val btnSignUp = findViewById<LinearLayout>(R.id.btnSignUp)

        btnSignIn.setOnClickListener {
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()

            if (email.isBlank() || password.isBlank()) {
                Toast.makeText(this, "Tüm alanları doldurun", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    val uid = it.user?.uid ?: return@addOnSuccessListener
                    SharedPreferencesUtils.setLoggedIn(this, true)
                    SharedPreferencesUtils.setUserId(this, uid)
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Giriş başarısız: ${it.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
        }

        btnSignUp.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
        SharedPreferencesUtils.applySavedBackgroundColor(this)
    }

    override fun attachBaseContext(newBase: Context?) {
        val lang = SharedPreferencesUtils.getAppLanguage(newBase!!)
        val context = LocaleHelper.setLocale(newBase, lang)
        super.attachBaseContext(context)
    }
}