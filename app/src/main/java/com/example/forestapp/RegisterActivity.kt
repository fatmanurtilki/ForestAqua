package com.example.forestapp

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

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
