package com.example.feature.auth.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserDao {

    // Inseram utilizator nou. Daca emailul exista deja, insert-ul esueaza.
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(user: UserEntity): Long

    // Cautam utilizator dupa email pentru login.
    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getByEmail(email: String): UserEntity?

    @androidx.room.Update
    suspend fun update(user: UserEntity)
}
