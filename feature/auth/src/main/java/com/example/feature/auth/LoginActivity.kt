package com.example.feature.auth

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Legam campurile din XML la codul Kotlin.
        val emailInput = findViewById<EditText>(R.id.emailInput)
        val passwordInput = findViewById<EditText>(R.id.passwordInput)
        val loginButton = findViewById<Button>(R.id.loginButton)

        loginButton.setOnClickListener {
            // Citim textul introdus de utilizator.
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()

            // Pentru moment validam doar ca nu sunt goale.
            if (email.isNotEmpty() && password.isNotEmpty()) {
                // In pasii urmatori vom verifica datele in Room.
                finish()
            }
        }
    }
}
