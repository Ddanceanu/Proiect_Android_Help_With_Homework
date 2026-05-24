package com.example.proiecthelpwithhomework

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HomeworkHistoryActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var btnPosted: Button
    private lateinit var btnSolved: Button
    private lateinit var btnBack: Button
    private val allHomeworks = mutableListOf<Homework>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homework_history)

        recyclerView = findViewById(R.id.historyRecyclerView)
        btnPosted = findViewById(R.id.btnPosted)
        btnSolved = findViewById(R.id.btnSolved)
        btnBack = findViewById(R.id.btnBack)

        recyclerView.layoutManager = LinearLayoutManager(this)

        // Date de test
        loadDummyData()

        // Inițial arătăm temele postate
        showPostedHomeworks()

        btnPosted.setOnClickListener {
            showPostedHomeworks()
        }

        btnSolved.setOnClickListener {
            showSolvedHomeworks()
        }

        btnBack.setOnClickListener {
            // Închide activitatea curentă și revine la cea anterioară (MainActivity)
            finish()
        }
    }

    private fun loadDummyData() {
        allHomeworks.add(Homework("Ecuații de gradul 2", "Matematică", "Ajutor delta", "1h", true, "Ion Popescu", "Profesor Mate"))
        allHomeworks.add(Homework("Eseu Enigma Otiliei", "Română", "Comentariu literar", "3h", false, "Ion Popescu", null))
        allHomeworks.add(Homework("Circuit electric", "Fizică", "Legile lui Kirchhoff", "2h", true, "Andrei Ionescu", "Ion Popescu"))
    }

    private fun showPostedHomeworks() {
        val posted = allHomeworks.filter { it.postedBy == "Ion Popescu" }
        recyclerView.adapter = HomeworkAdapter(posted.toMutableList(), showActions = false)
    }

    private fun showSolvedHomeworks() {
        val solved = allHomeworks.filter { it.solvedBy == "Ion Popescu" }
        recyclerView.adapter = HomeworkAdapter(solved.toMutableList(), showActions = false)
    }
}
