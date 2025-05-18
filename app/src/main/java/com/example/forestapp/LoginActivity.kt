package com.example.forestapp
// mail-kullanıcı adı olarak farklılıkta düzenleme yapman lazım.
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

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

        val radioGroup = findViewById<RadioGroup>(R.id.rgLoginType)

        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rbEmail -> etIdentifier.hint = "Email"
                R.id.rbUsername -> etIdentifier.hint = "Kullanıcı Adı"
            }
        }

        btnSignIn.setOnClickListener {
            val identifier = etIdentifier.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (identifier.isNotEmpty() && password.isNotEmpty()) {
                val db = dbHelper.readableDatabase
                val column = if (rbEmail.isChecked) "email" else "username"
                val query = "SELECT * FROM users WHERE $column = ? AND password = ?"
                val cursor = db.rawQuery(query, arrayOf(identifier, password))

                if (cursor.moveToFirst()) {
                    cursor.close()
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else {
                    cursor.close()
                    Toast.makeText(this, "Bilgiler hatalı", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Tüm alanları doldurun", Toast.LENGTH_SHORT).show()
            }
        }

        btnSignUp.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}
