package com.example.forestapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

import com.example.forestapp.util.LocaleHelper
import com.example.forestapp.util.SharedPreferencesUtils
>>>>>> Stashed changes

class RegisterActivity : AppCompatActivity() {

    private lateinit var dbHelper: ForestDbHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        dbHelper = ForestDbHelper(this)

        val etName = findViewById<EditText>(R.id.etName)
        val etUsername = findViewById<EditText>(R.id.etUsername)
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)

        val btnBack = findViewById<LinearLayout>(R.id.btnBackLayout)
<<<<<<< Updated upstream
=======

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
                    userRepo.createUser(
                        uid, user,
                        onSuccess = {
                            Toast.makeText(this, "Kayıt başarılı", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this, LoginActivity::class.java))
                            finish()
                        },
                        onFailure = {
                            Toast.makeText(this, "Firestore'a kayıt başarısız", Toast.LENGTH_SHORT)
                                .show()
                        })
                }
                .addOnFailureListener {
                    Toast.makeText(
                        this,
                        "Kayıt başarısız: ${it.localizedMessage}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }

>>>>>>> Stashed changes
        btnBack.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
<<<<<<< Updated upstream
        val btnRegister = findViewById<LinearLayout>(R.id.btnRegisterLayout)
        btnRegister.setOnClickListener {
            val name = etName.text.toString().trim()
            val username = etUsername.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (name.isEmpty() || username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Tüm alanları doldurun", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Geçerli bir email girin", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val db = dbHelper.readableDatabase
            val cursorEmail = db.rawQuery("SELECT * FROM users WHERE email = ?", arrayOf(email))
            val cursorUsername = db.rawQuery("SELECT * FROM users WHERE username = ?", arrayOf(username))

            if (cursorEmail.moveToFirst()) {
                Toast.makeText(this, "Email adresi ile hesap bulunmaktadır", Toast.LENGTH_SHORT).show()
                cursorEmail.close()
                cursorUsername.close()
                return@setOnClickListener
            }

            if (cursorUsername.moveToFirst()) {
                Toast.makeText(this, "Kullanıcı adı ile hesap bulunmaktadır", Toast.LENGTH_SHORT).show()
                cursorEmail.close()
                cursorUsername.close()
                return@setOnClickListener
            }

            cursorEmail.close()
            cursorUsername.close()

            val writableDb = dbHelper.writableDatabase
            writableDb.execSQL(
                "INSERT INTO users (name, username, email, password, coins, total_focus_time, trees_planted, real_trees_planted, daily_goal) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)",
                arrayOf(name, username, email, password, 0, 0, 0, 0, 25)
            )

            Toast.makeText(this, "Kayıt başarılı! Giriş yapabilirsiniz", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()

        }
    }
}
=======
        SharedPreferencesUtils.applySavedBackgroundColor(this)
        setContentView(R.layout.activity_register)
    }
        override fun attachBaseContext(newBase: Context?) {
            val lang = SharedPreferencesUtils.getAppLanguage(newBase!!)
            val context = LocaleHelper.setLocale(newBase, lang)
            super.attachBaseContext(context)
        }
}
>>>>>>> Stashed changes
