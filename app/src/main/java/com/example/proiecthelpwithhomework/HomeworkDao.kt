package com.example.proiecthelpwithhomework

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface HomeworkDao {
    @Query("SELECT * FROM homework_table ORDER BY id DESC")
    fun getAllHomework(): Flow<List<Homework>>

    @Query("SELECT * FROM homework_table WHERE title LIKE :searchQuery OR subject LIKE :searchQuery ORDER BY title ASC")
    fun searchHomework(searchQuery: String): Flow<List<Homework>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(homework: Homework)

    @Update
    suspend fun update(homework: Homework)

    @Delete
    suspend fun delete(homework: Homework)
}