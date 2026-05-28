package com.example.proiecthelpwithhomework

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class HomeworkDetailActivity : AppCompatActivity() {

    private val viewModel: HomeworkViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homework_detail)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { finish() }

        val id = intent.getIntExtra("id", -1)
        val title = intent.getStringExtra("title") ?: ""
        val subject = intent.getStringExtra("subject") ?: ""
        val description = intent.getStringExtra("description") ?: ""
        val solution = intent.getStringExtra("solution")

        findViewById<TextView>(R.id.detailTitle).text = title
        findViewById<TextView>(R.id.detailSubject).text = subject
        findViewById<TextView>(R.id.detailDescription).text = description
        
        val solutionTextView = findViewById<TextView>(R.id.detailSolution)
        val addSolutionLayout = findViewById<LinearLayout>(R.id.addSolutionLayout)

        if (solution.isNullOrEmpty()) {
            solutionTextView.text = "No solution added yet."
            addSolutionLayout.visibility = View.VISIBLE
        } else {
            solutionTextView.text = solution
            addSolutionLayout.visibility = View.GONE
        }

        findViewById<Button>(R.id.saveSolutionButton).setOnClickListener {
            val newSolution = findViewById<EditText>(R.id.solutionInput).text.toString()
            if (newSolution.isNotEmpty()) {
                solutionTextView.text = newSolution
                addSolutionLayout.visibility = View.GONE
                
                // Update in database
                val updatedHomework = Homework(
                    id = id,
                    title = title,
                    subject = subject,
                    description = description,
                    duration = intent.getStringExtra("duration") ?: "",
                    isSolved = true,
                    solution = newSolution
                )
                viewModel.update(updatedHomework)
            }
        }
    }
}