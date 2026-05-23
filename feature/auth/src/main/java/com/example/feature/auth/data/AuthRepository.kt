package com.example.feature.auth.data

import com.example.feature.auth.data.local.UserDao
import com.example.feature.auth.data.local.UserEntity
import java.security.MessageDigest

class AuthRepository(private val userDao: UserDao) {

    suspend fun register(name: String, email: String, password: String): Boolean {
        return try {
            // Daca exista deja emailul, nu mai cream cont nou.
            val existingUser = userDao.getByEmail(email)
            if (existingUser != null) return false

            val user = UserEntity(
                name = name,
                email = email,
                passwordHash = hashPassword(password)
            )

            userDao.insert(user)
            true
        } catch (_: Exception) {
            // Evitam crash-ul: daca baza da eroare, intoarcem false.
            false
        }
    }

    suspend fun login(email: String, password: String): Boolean {
        return try {
            val user = userDao.getByEmail(email) ?: return false
            user.passwordHash == hashPassword(password)
        } catch (_: Exception) {
            // Evitam crash-ul: orice eroare de DB intoarce login nereusit.
            false
        }
    }

    private fun hashPassword(password: String): String {
        // Pentru tema este suficient SHA-256, ca sa nu tinem parola in clar.
        val bytes = MessageDigest.getInstance("SHA-256").digest(password.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }
}
