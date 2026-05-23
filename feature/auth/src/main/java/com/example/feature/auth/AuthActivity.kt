package com.example.feature.auth

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.feature.auth.data.AuthRepository
import com.example.feature.auth.data.local.AppDatabase
import kotlinx.coroutines.launch

class AuthActivity : AppCompatActivity() {

    companion object {
        // Cheie fixa pentru navigare spre ecranul principal.
        private const val ACTION_OPEN_MAIN = "com.example.proiecthelpwithhomework.action.OPEN_MAIN"
    }

    private var isLoginMode = true
    private lateinit var authRepository: AuthRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        val titleText = findViewById<TextView>(R.id.titleText)
        val loginTab = findViewById<Button>(R.id.loginTab)
        val registerTab = findViewById<Button>(R.id.registerTab)
        val nameInput = findViewById<EditText>(R.id.nameInput)
        val emailInput = findViewById<EditText>(R.id.emailInput)
        val passwordInput = findViewById<EditText>(R.id.passwordInput)
        val confirmPasswordInput = findViewById<EditText>(R.id.confirmPasswordInput)
        val submitButton = findViewById<Button>(R.id.submitButton)
        authRepository = AuthRepository(AppDatabase.getInstance(this).userDao())

        fun updateModeUi() {
            if (isLoginMode) {
                titleText.text = "Login"
                loginTab.isEnabled = false
                registerTab.isEnabled = true
                nameInput.visibility = View.GONE
                confirmPasswordInput.visibility = View.GONE
                submitButton.text = "Intra in cont"
            } else {
                titleText.text = "Register"
                loginTab.isEnabled = true
                registerTab.isEnabled = false
                nameInput.visibility = View.VISIBLE
                confirmPasswordInput.visibility = View.VISIBLE
                submitButton.text = "Creeaza cont"
            }

            // Curatam campurile cand schimbam modul.
            nameInput.text.clear()
            emailInput.text.clear()
            passwordInput.text.clear()
            confirmPasswordInput.text.clear()
            nameInput.error = null
            emailInput.error = null
            passwordInput.error = null
            confirmPasswordInput.error = null
        }

        loginTab.setOnClickListener {
            isLoginMode = true
            updateModeUi()
        }

        registerTab.setOnClickListener {
            isLoginMode = false
            updateModeUi()
        }

        submitButton.setOnClickListener {
            val name = nameInput.text.toString().trim()
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()
            val confirmPassword = confirmPasswordInput.text.toString().trim()

            nameInput.error = null
            emailInput.error = null
            passwordInput.error = null
            confirmPasswordInput.error = null

            if (!isLoginMode && name.isEmpty()) {
                nameInput.error = "Completeaza numele"
                nameInput.requestFocus()
                return@setOnClickListener
            }

            if (email.isEmpty()) {
                emailInput.error = "Completeaza emailul"
                emailInput.requestFocus()
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                emailInput.error = "Email invalid"
                emailInput.requestFocus()
                return@setOnClickListener
            }

            if (password.length < 6) {
                passwordInput.error = "Parola trebuie sa aiba minim 6 caractere"
                passwordInput.requestFocus()
                return@setOnClickListener
            }

            if (!isLoginMode && password != confirmPassword) {
                confirmPasswordInput.error = "Parolele nu coincid"
                confirmPasswordInput.requestFocus()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                if (isLoginMode) {
                    val loginOk = authRepository.login(email, password)
                    if (!loginOk) {
                        Toast.makeText(
                            this@AuthActivity,
                            "Email sau parola incorecta",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@launch
                    }
                } else {
                    val registerOk = authRepository.register(name, email, password)
                    if (!registerOk) {
                        emailInput.error = "Exista deja cont cu acest email"
                        emailInput.requestFocus()
                        return@launch
                    }
                }

                // Daca login/register este ok, intram in ecranul principal.
                startActivity(Intent(ACTION_OPEN_MAIN))
                finish()
            }
        }

        // Pornim direct in modul Login.
        updateModeUi()
    }
}
