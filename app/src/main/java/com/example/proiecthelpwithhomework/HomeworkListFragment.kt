package com.example.proiecthelpwithhomework

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HomeworkListFragment : Fragment() {

    private val viewModel: HomeworkViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_homework_list, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        val searchEditText = view.findViewById<EditText>(R.id.searchEditText)
        val sortButton = view.findViewById<View>(R.id.sortButton)
        
        recyclerView.layoutManager = LinearLayoutManager(context)
        
        val adapter = HomeworkAdapter(
            mutableListOf(),
            showActions = false,
            onClick = { homework ->
                val intent = Intent(context, HomeworkDetailActivity::class.java).apply {
                    putExtra("id", homework.id)
                    putExtra("title", homework.title)
                    putExtra("subject", homework.subject)
                    putExtra("description", homework.description)
                    putExtra("solution", homework.solution)
                    putExtra("duration", homework.duration)
                }
                startActivity(intent)
            }
        )
        recyclerView.adapter = adapter

        viewModel.allHomework.observe(viewLifecycleOwner) { homeworks ->
            adapter.updateData(homeworks)
        }

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.setSearchQuery(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        sortButton.setOnClickListener {
            // Rudimentary sort toggle: Title vs ID
            val currentList = viewModel.allHomework.value ?: return@setOnClickListener
            val sortedList = if (currentList.firstOrNull()?.title == currentList.sortedBy { it.title }.firstOrNull()?.title) {
                currentList.sortedByDescending { it.id }
            } else {
                currentList.sortedBy { it.title }
            }
            adapter.updateData(sortedList)
        }

        return view
    }
}