package com.example.proiecthelpwithhomework

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class HomeworkAdapter(
    private val list: MutableList<Homework>,
    private val onDelete: (Homework) -> Unit
) : RecyclerView.Adapter<HomeworkAdapter.HomeworkViewHolder>() {

    class HomeworkViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.titleText)
        val subject: TextView = itemView.findViewById(R.id.subjectText)
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

        // CLICK = DELETE
        holder.itemView.setOnClickListener {
            onDelete(item)
            list.removeAt(position)
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int = list.size
}