package com.example.proiecthelpwithhomework

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import com.example.feature.auth.data.AuthRepository
import com.example.feature.auth.data.local.AppDatabase as AuthDatabase
import com.example.feature.auth.data.session.SessionManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class AccountSettingsActivity : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager
    private lateinit var authRepository: AuthRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_settings)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { finish() }

        sessionManager = SessionManager(this)
        val userDao = AuthDatabase.getInstance(this).userDao()
        authRepository = AuthRepository(userDao)

        val editName = findViewById<EditText>(R.id.editName)
        val btnUpdateName = findViewById<Button>(R.id.btnUpdateName)

        val editCurrentPassword = findViewById<EditText>(R.id.editCurrentPassword)
        val editNewPassword = findViewById<EditText>(R.id.editNewPassword)
        val editConfirmNewPassword = findViewById<EditText>(R.id.editConfirmNewPassword)
        val btnUpdatePassword = findViewById<Button>(R.id.btnUpdatePassword)

        lifecycleScope.launch {
            val currentName = sessionManager.userNameFlow.first()
            editName.setText(currentName)
        }

        btnUpdateName.setOnClickListener {
            val newName = editName.text.toString().trim()
            if (newName.isNotEmpty()) {
                lifecycleScope.launch {
                    val email = sessionManager.userEmailFlow.first()
                    if (email != null) {
                        val user = userDao.getByEmail(email)
                        if (user != null) {
                            user.name = newName
                            userDao.update(user)
                            sessionManager.updateName(newName)
                            Toast.makeText(this@AccountSettingsActivity, "Nume actualizat!", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } else {
                editName.error = "Numele nu poate fi gol"
            }
        }

        btnUpdatePassword.setOnClickListener {
            val currentPass = editCurrentPassword.text.toString()
            val newPass = editNewPassword.text.toString()
            val confirmPass = editConfirmNewPassword.text.toString()

            if (currentPass.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty()) {
                Toast.makeText(this, "Completează toate câmpurile", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (newPass.length < 6) {
                editNewPassword.error = "Minim 6 caractere"
                return@setOnClickListener
            }

            if (newPass != confirmPass) {
                editConfirmNewPassword.error = "Parolele nu coincid"
                return@setOnClickListener
            }

            lifecycleScope.launch {
                val email = sessionManager.userEmailFlow.first()
                if (email != null) {
                    val user = userDao.getByEmail(email)
                    if (user != null) {
                        val currentHash = authRepository.hashPassword(currentPass)
                        if (user.passwordHash == currentHash) {
                            user.passwordHash = authRepository.hashPassword(newPass)
                            userDao.update(user)
                            editCurrentPassword.text.clear()
                            editNewPassword.text.clear()
                            editConfirmNewPassword.text.clear()
                            Toast.makeText(this@AccountSettingsActivity, "Parolă actualizată!", Toast.LENGTH_SHORT).show()
                        } else {
                            editCurrentPassword.error = "Parola curentă incorectă"
                        }
                    }
                }
            }
        }
    }
}
