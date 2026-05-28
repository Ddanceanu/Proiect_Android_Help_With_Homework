package com.example.proiecthelpwithhomework

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class HomeworkAdapter(
    private var list: MutableList<Homework>,
    private val showActions: Boolean = true,
    private val onEdit: ((Homework, Int) -> Unit)? = null,
    private val onDelete: ((Int) -> Unit)? = null,
    private val onSolve: ((Int) -> Unit)? = null,
    private val onClick: ((Homework) -> Unit)? = null
) : RecyclerView.Adapter<HomeworkAdapter.HomeworkViewHolder>() {

    class HomeworkViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.titleText)
        val subject: TextView = itemView.findViewById(R.id.subjectText)
        val description: TextView = itemView.findViewById(R.id.descriptionText)
        val duration: TextView = itemView.findViewById(R.id.durationText)
        val userBy: TextView = itemView.findViewById(R.id.userByText)
        val actionButtons: LinearLayout = itemView.findViewById(R.id.actionButtons)
        val editButton: Button = itemView.findViewById(R.id.editButton)
        val solveButton: Button = itemView.findViewById(R.id.solveButton)
        val deleteButton: Button = itemView.findViewById(R.id.deleteButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeworkViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_homework, parent, false)
        return HomeworkViewHolder(view)
    }

    override fun onBindViewHolder(holder: HomeworkViewHolder, position: Int) {
        val item = list[position]
        holder.title.text = item.title
        holder.subject.text = item.subject
        holder.description.text = item.description
        holder.duration.text = "Timp: ${item.duration}"
        
        val userInfo = StringBuilder("Postat de: ${item.postedBy}")
        if (item.solvedBy != null) {
            userInfo.append(" | Rezolvat de: ${item.solvedBy}")
        }
        holder.userBy.text = userInfo.toString()

        if (item.isSolved) {
            holder.title.paintFlags = holder.title.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            holder.solveButton.text = "Anulează"
        } else {
            holder.title.paintFlags = holder.title.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            holder.solveButton.text = "Rezolvat"
        }

        if (showActions) {
            holder.actionButtons.visibility = View.VISIBLE
            holder.editButton.setOnClickListener { onEdit?.invoke(item, position) }
            holder.deleteButton.setOnClickListener { onDelete?.invoke(position) }
            holder.solveButton.setOnClickListener { onSolve?.invoke(position) }
        } else {
            holder.actionButtons.visibility = View.GONE
        }

        holder.itemView.setOnClickListener {
            onClick?.invoke(item)
        }
    }

    override fun getItemCount(): Int = list.size

    fun updateData(newList: List<Homework>) {
        this.list = newList.toMutableList()
        notifyDataSetChanged()
    }
}