package com.example.forestapp

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.forestapp.util.SharedPreferencesUtils

class LoginActivity : AppCompatActivity() {

    private lateinit var dbHelper: ForestDbHelper
    private lateinit var etIdentifier: EditText
    private lateinit var etPassword: EditText
    private lateinit var rbEmail: RadioButton
    private lateinit var rbUsername: RadioButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        dbHelper = ForestDbHelper(this)

        etIdentifier = findViewById(R.id.etIdentifier)
        etPassword = findViewById(R.id.etPassword)
        rbEmail = findViewById(R.id.rbEmail)
        rbUsername = findViewById(R.id.rbUsername)

        val btnSignIn = findViewById<LinearLayout>(R.id.btnSignIn)
        val btnSignUp = findViewById<LinearLayout>(R.id.btnSignUp)

        findViewById<RadioGroup>(R.id.rgLoginType).setOnCheckedChangeListener { _, checkedId ->
            etIdentifier.hint = if (checkedId == R.id.rbEmail) "Email" else "Kullanıcı Adı"
        }

        btnSignIn.setOnClickListener {
            val identifier = etIdentifier.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (identifier.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Lütfen tüm alanları doldurun", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val column = if (rbEmail.isChecked) "email" else "username"
            val db = dbHelper.readableDatabase
            val cursor = db.rawQuery(
                "SELECT * FROM users WHERE $column = ? AND password = ?",
                arrayOf(identifier, password)
            )

            if (cursor.moveToFirst()) {
                val userId = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                SharedPreferencesUtils.setLoggedIn(this, true)
                SharedPreferencesUtils.setUserId(this, userId)
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "Kayıtlı kullanıcı bulunamadı", Toast.LENGTH_SHORT).show()
            }

            cursor.close()
        }

        btnSignUp.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}
