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
import androidx.lifecycle.lifecycleScope
import com.example.feature.auth.data.session.SessionManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class CreateHomeworkFragment : Fragment() {

    private val viewModel: HomeworkViewModel by activityViewModels()
    private lateinit var sessionManager: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_create_homework, container, false)
        
        sessionManager = SessionManager(requireContext())

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
                lifecycleScope.launch {
                    val userName = sessionManager.userNameFlow.first() ?: "Anonim"
                    val homework = Homework(
                        title = title,
                        subject = subject,
                        description = description,
                        duration = duration,
                        postedBy = userName
                    )
                    viewModel.insert(homework)
                    
                    titleInput.text.clear()
                    subjectInput.text.clear()
                    descriptionInput.text.clear()
                    durationInput.text.clear()
                    
                    Toast.makeText(context, "Temă postată!", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "Te rugăm să completezi titlul și materia", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }
}