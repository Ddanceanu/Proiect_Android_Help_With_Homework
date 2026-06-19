package com.example.proiecthelpwithhomework

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface HomeworkDao {
    @Query("SELECT * FROM homework_table ORDER BY id DESC")
    fun getAllHomework(): Flow<List<Homework>>

    @Query("SELECT * FROM homework_table WHERE title LIKE :searchQuery OR subject LIKE :searchQuery ORDER BY title ASC")
    fun searchHomework(searchQuery: String): Flow<List<Homework>>

    @Query("SELECT * FROM homework_table WHERE postedBy = :userName ORDER BY id DESC")
    fun getHomeworkByUser(userName: String): Flow<List<Homework>>

    @Query("SELECT * FROM homework_table WHERE solvedBy = :userName ORDER BY id DESC")
    fun getSolvedHomeworkByUser(userName: String): Flow<List<Homework>>

    @Query("SELECT * FROM homework_table WHERE id = :id LIMIT 1")
    fun getHomeworkById(id: Int): Flow<Homework?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(homework: Homework): Long

    @Update
    suspend fun update(homework: Homework)

    @Delete
    suspend fun delete(homework: Homework)
}