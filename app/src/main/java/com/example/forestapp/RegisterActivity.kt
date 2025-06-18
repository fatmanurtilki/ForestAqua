package com.example.forestapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.forestapp.model.User
import com.example.forestapp.repository.UserRepository
import com.example.forestapp.util.SharedPreferencesUtils
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private val userRepo = UserRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        val rootView: View = findViewById(android.R.id.content)
        SharedPreferencesUtils.applySavedBackgroundColor(this, rootView)
        auth = FirebaseAuth.getInstance()

        val etName = findViewById<EditText>(R.id.etName)
        val etUsername = findViewById<EditText>(R.id.etUsername)
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnRegister = findViewById<LinearLayout>(R.id.btnRegisterLayout)
        val btnBack = findViewById<LinearLayout>(R.id.btnBackLayout)

        btnRegister.setOnClickListener {
            val name = etName.text.toString()
            val username = etUsername.text.toString()
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()

            if (name.isBlank() || username.isBlank() || email.isBlank() || password.isBlank()) {
                Toast.makeText(this, "Tüm alanları doldurun", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    val uid = it.user?.uid ?: return@addOnSuccessListener
                    val user = User(
                        id = uid,
                        name = name,
                        username = username,
                        email = email,
                        password = password
                    )
                    userRepo.createUser(uid, user,
                        onSuccess = {
                            Toast.makeText(this, "Kayıt başarılı", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this, LoginActivity::class.java))
                            finish()
                        },
                        onFailure = {
                            Toast.makeText(this, "Firestore'a kayıt başarısız", Toast.LENGTH_SHORT).show()
                        })
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Kayıt başarısız: ${it.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
        }

        btnBack.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
    override fun attachBaseContext(newBase: Context) {
        val lang = SharedPreferencesUtils.getAppLanguage(newBase)
        val contextWithLocale = LanguageHelper.setLocale(newBase, lang)
        super.attachBaseContext(contextWithLocale)
    }
}