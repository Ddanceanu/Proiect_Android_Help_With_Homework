package com.example.proiecthelpwithhomework

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val addButton = findViewById<Button>(R.id.addButton)
        val titleInput = findViewById<EditText>(R.id.titleInput)
        val subjectInput = findViewById<EditText>(R.id.subjectInput)

        recyclerView.layoutManager = LinearLayoutManager(this)

        val homeworkList = mutableListOf(
            Homework("Ecuații de gradul 2", "Matematică"),
            Homework("Algoritm sortare", "Informatică"),
            Homework("Legea lui Ohm", "Fizică")
        )

        val adapter = HomeworkAdapter(homeworkList)
        recyclerView.adapter = adapter

        addButton.setOnClickListener {
            val title = titleInput.text.toString()
            val subject = subjectInput.text.toString()

            if (title.isNotEmpty() && subject.isNotEmpty()) {
                homeworkList.add(Homework(title, subject))
                adapter.notifyDataSetChanged()

                titleInput.text.clear()
                subjectInput.text.clear()
            }
        }
    }
}