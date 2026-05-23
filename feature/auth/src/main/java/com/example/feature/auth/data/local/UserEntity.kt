package com.example.feature.auth.data.local

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

// Tabelul in care pastram utilizatorii aplicatiei.
@Entity(
    tableName = "users",
    indices = [Index(value = ["email"], unique = true)]
)
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val email: String,
    val passwordHash: String,
    val createdAt: Long = System.currentTimeMillis()
)
