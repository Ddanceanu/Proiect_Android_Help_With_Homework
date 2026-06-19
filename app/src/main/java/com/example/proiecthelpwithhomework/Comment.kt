package com.example.proiecthelpwithhomework

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "comment_table")
data class Comment(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val homeworkId: Int,
    val authorName: String,
    val text: String,
    val timestamp: Long = System.currentTimeMillis(),
    var likes: Int = 0,
    var dislikes: Int = 0
)