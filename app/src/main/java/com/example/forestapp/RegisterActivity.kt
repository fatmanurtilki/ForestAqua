// butonla main e geçişi düzenle
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

            if (!name.matches(Regex("[a-zA-ZğüşöçıİĞÜŞÖÇ ]+"))) {
                Toast.makeText(this, "Ad-Soyad sadece harf içermelidir", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!username.matches(Regex("[a-zA-Z0-9._-]+"))) {
                Toast.makeText(this, "Kullanıcı adı geçersiz karakter içeriyor", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Geçerli bir email adresi giriniz", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val db = dbHelper.readableDatabase
            val cursorUsername = db.rawQuery("SELECT * FROM users WHERE username = ?", arrayOf(username))
            val cursorEmail = db.rawQuery("SELECT * FROM users WHERE email = ?", arrayOf(email))

            if (cursorUsername.moveToFirst()) {
                Toast.makeText(this, "$username kullanılmaktadır, başka bir kullanıcı adı giriniz", Toast.LENGTH_SHORT).show()
                cursorUsername.close()
                cursorEmail.close()
                return@setOnClickListener
            }

            if (cursorEmail.moveToFirst()) {
                Toast.makeText(this, "$email kullanılmaktadır, başka bir email giriniz", Toast.LENGTH_SHORT).show()
                cursorUsername.close()
                cursorEmail.close()
                return@setOnClickListener
            }

            cursorUsername.close()
            cursorEmail.close()

            val writableDb = dbHelper.writableDatabase
            writableDb.execSQL(
                "INSERT INTO users (name, username, email, password) VALUES (?, ?, ?, ?)",
                arrayOf(name, username, email, password)
            )

            Toast.makeText(this, "Kayıt başarılı!", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}
