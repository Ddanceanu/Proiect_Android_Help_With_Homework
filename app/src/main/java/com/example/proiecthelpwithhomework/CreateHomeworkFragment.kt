package com.example.proiecthelpwithhomework

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels

class CreateHomeworkFragment : Fragment() {

    private val viewModel: HomeworkViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_create_homework, container, false)

        val titleInput = view.findViewById<EditText>(R.id.titleInput)
        val subjectInput = view.findViewById<EditText>(R.id.subjectInput)
        val descriptionInput = view.findViewById<EditText>(R.id.descriptionInput)
        val durationInput = view.findViewById<EditText>(R.id.durationInput)
        val addButton = view.findViewById<Button>(R.id.addButton)

        addButton.setOnClickListener {
            val title = titleInput.text.toString()
            val subject = subjectInput.text.toString()
            val description = descriptionInput.text.toString()
            val duration = durationInput.text.toString()

            if (title.isNotEmpty() && subject.isNotEmpty()) {
                val homework = Homework(
                    title = title,
                    subject = subject,
                    description = description,
                    duration = duration
                )
                viewModel.insert(homework)
                
                titleInput.text.clear()
                subjectInput.text.clear()
                descriptionInput.text.clear()
                durationInput.text.clear()
                
                Toast.makeText(context, "Homework posted!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Please fill title and subject", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }
}