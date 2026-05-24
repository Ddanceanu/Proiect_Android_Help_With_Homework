package com.example.proiecthelpwithhomework

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.feature.auth.AuthActivity
import com.example.feature.auth.data.session.SessionManager
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val addButton = findViewById<Button>(R.id.addButton)
        val logoutButton = findViewById<Button>(R.id.logoutButton)
        val historyButton = findViewById<Button>(R.id.historyButton)
        val titleInput = findViewById<EditText>(R.id.titleInput)
        val subjectInput = findViewById<EditText>(R.id.subjectInput)
        val descriptionInput = findViewById<EditText>(R.id.descriptionInput)
        val durationInput = findViewById<EditText>(R.id.durationInput)
        
        val sessionManager = SessionManager(this)

        recyclerView.layoutManager = LinearLayoutManager(this)

        val homeworkList = mutableListOf(
            Homework("Ecuații de gradul 2", "Matematică", "Am nevoie de ajutor la formula deltei.", "1 oră"),
            Homework("Algoritm sortare", "Informatică", "Nu înțeleg recursivitatea la QuickSort.", "2 ore"),
            Homework("Legea lui Ohm", "Fizică", "Probleme cu rezistența echivalentă.", "45 min")
        )

        lateinit var adapter: HomeworkAdapter

        adapter = HomeworkAdapter(
            homeworkList,
            showActions = true,
            onEdit = { homework, position ->
                titleInput.setText(homework.title)
                subjectInput.setText(homework.subject)
                descriptionInput.setText(homework.description)
                durationInput.setText(homework.duration)
                
                homeworkList.removeAt(position)
                adapter.notifyDataSetChanged()
                Toast.makeText(this, "Editează câmpurile și apasă Adaugă", Toast.LENGTH_SHORT).show()
            },
            onDelete = { position ->
                homeworkList.removeAt(position)
                adapter.notifyDataSetChanged()
            },
            onSolve = { position ->
                homeworkList[position].isSolved = !homeworkList[position].isSolved
                adapter.notifyDataSetChanged()
            }
        )
        
        recyclerView.adapter = adapter

        addButton.setOnClickListener {
            val title = titleInput.text.toString()
            val subject = subjectInput.text.toString()
            val description = descriptionInput.text.toString()
            val duration = durationInput.text.toString()

            if (title.isNotEmpty() && subject.isNotEmpty()) {
                homeworkList.add(Homework(title, subject, description, duration))
                adapter.notifyDataSetChanged()

                titleInput.text.clear()
                subjectInput.text.clear()
                descriptionInput.text.clear()
                durationInput.text.clear()
            } else {
                Toast.makeText(this, "Te rugăm să completezi titlul și materia", Toast.LENGTH_SHORT).show()
            }
        }

        historyButton.setOnClickListener {
            startActivity(Intent(this, HomeworkHistoryActivity::class.java))
        }

        logoutButton.setOnClickListener {
            lifecycleScope.launch {
                sessionManager.setLoggedIn(false)
                startActivity(Intent(this@MainActivity, AuthActivity::class.java))
                finish()
            }
        }
    }
}
