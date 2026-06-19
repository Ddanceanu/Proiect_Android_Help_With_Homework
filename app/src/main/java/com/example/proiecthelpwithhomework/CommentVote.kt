package com.example.proiecthelpwithhomework

import androidx.room.Entity

@Entity(tableName = "comment_vote_table", primaryKeys = ["commentId", "userName"])
data class CommentVote(
    val commentId: Int,
    val userName: String,
    val voteType: Int // 1 for LIKE, -1 for DISLIKE
)