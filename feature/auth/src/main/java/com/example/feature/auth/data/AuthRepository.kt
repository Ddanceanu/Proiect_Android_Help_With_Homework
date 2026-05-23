package com.example.feature.auth.data

import com.example.feature.auth.data.local.UserDao
import com.example.feature.auth.data.local.UserEntity
import java.security.MessageDigest

class AuthRepository(private val userDao: UserDao) {

    suspend fun register(name: String, email: String, password: String): Boolean {
        // Daca exista deja emailul, nu mai cream cont nou.
        val existingUser = userDao.getByEmail(email)
        if (existingUser != null) return false

        val user = UserEntity(
            name = name,
            email = email,
            passwordHash = hashPassword(password)
        )

        userDao.insert(user)
        return true
    }

    suspend fun login(email: String, password: String): Boolean {
        val user = userDao.getByEmail(email) ?: return false
        return user.passwordHash == hashPassword(password)
    }

    private fun hashPassword(password: String): String {
        // Pentru tema este suficient SHA-256, ca sa nu tinem parola in clar.
        val bytes = MessageDigest.getInstance("SHA-256").digest(password.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }
}
