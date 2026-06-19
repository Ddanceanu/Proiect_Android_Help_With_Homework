package com.example.proiecthelpwithhomework

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CommentDao {
    @Query("SELECT * FROM comment_table WHERE homeworkId = :homeworkId ORDER BY (likes - dislikes) DESC, id DESC")
    fun getCommentsForHomework(homeworkId: Int): Flow<List<Comment>>

    @Insert
    suspend fun insert(comment: Comment)

    @Update
    suspend fun update(comment: Comment)

    @Delete
    suspend fun delete(comment: Comment)

    @Query("SELECT * FROM comment_table WHERE id = :id")
    suspend fun getCommentById(id: Int): Comment?

    // Vote related
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVote(vote: CommentVote)

    @Query("SELECT * FROM comment_vote_table WHERE commentId = :commentId AND userName = :userName")
    suspend fun getVote(commentId: Int, userName: String): CommentVote?

    @Query("DELETE FROM comment_vote_table WHERE commentId = :commentId AND userName = :userName")
    suspend fun deleteVote(commentId: Int, userName: String)
}