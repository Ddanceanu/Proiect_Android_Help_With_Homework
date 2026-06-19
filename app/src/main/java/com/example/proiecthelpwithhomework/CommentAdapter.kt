package com.example.proiecthelpwithhomework

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CommentAdapter(
    private var list: List<Comment>,
    private val currentUserName: String?,
    private val postAuthorName: String,
    private var acceptedCommentId: Int?,
    private val onLike: (Comment) -> Unit,
    private val onDislike: (Comment) -> Unit,
    private val onAccept: (Comment) -> Unit
) : RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {

    class CommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val author: TextView = itemView.findViewById(R.id.commentAuthor)
        val text: TextView = itemView.findViewById(R.id.commentText)
        val likes: TextView = itemView.findViewById(R.id.likesText)
        val dislikes: TextView = itemView.findViewById(R.id.dislikesText)
        val likeBtn: ImageButton = itemView.findViewById(R.id.likeButton)
        val dislikeBtn: ImageButton = itemView.findViewById(R.id.dislikeButton)
        val acceptBtn: Button = itemView.findViewById(R.id.acceptButton)
        val acceptedLabel: View = itemView.findViewById(R.id.acceptedLabel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_comment, parent, false)
        return CommentViewHolder(view)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val item = list[position]
        holder.author.text = item.authorName
        holder.text.text = item.text
        holder.likes.text = item.likes.toString()
        holder.dislikes.text = item.dislikes.toString()

        holder.likeBtn.setOnClickListener { onLike(item) }
        holder.dislikeBtn.setOnClickListener { onDislike(item) }

        // Visibility logic for accepted status
        if (item.id == acceptedCommentId) {
            holder.acceptedLabel.visibility = View.VISIBLE
            holder.acceptBtn.visibility = View.GONE
        } else {
            holder.acceptedLabel.visibility = View.GONE
            // Only show accept button to the post author if no solution is accepted yet
            if (currentUserName == postAuthorName && acceptedCommentId == null) {
                holder.acceptBtn.visibility = View.VISIBLE
                holder.acceptBtn.setOnClickListener { onAccept(item) }
            } else {
                holder.acceptBtn.visibility = View.GONE
            }
        }
    }

    override fun getItemCount(): Int = list.size

    fun getList(): List<Comment> = list

    fun updateData(newList: List<Comment>, newAcceptedId: Int?) {
        this.list = newList
        this.acceptedCommentId = newAcceptedId
        notifyDataSetChanged()
    }
}