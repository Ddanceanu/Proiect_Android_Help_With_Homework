package com.example.proiecthelpwithhomework

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "homework_table")
data class Homework(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    var title: String,
    var subject: String,
    var description: String,
    var duration: String,
    var isSolved: Boolean = false,
    var postedBy: String,
    var solvedBy: String? = null,
    var solution: String? = null,
    var acceptedCommentId: Int? = null
)