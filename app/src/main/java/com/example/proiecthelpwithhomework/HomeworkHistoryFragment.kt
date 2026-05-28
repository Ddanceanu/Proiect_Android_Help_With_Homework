package com.example.proiecthelpwithhomework

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HomeworkHistoryFragment : Fragment() {

    private val viewModel: HomeworkViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_homework_history, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.historyRecyclerView)
        val btnPosted = view.findViewById<Button>(R.id.btnPosted)
        val btnSolved = view.findViewById<Button>(R.id.btnSolved)

        recyclerView.layoutManager = LinearLayoutManager(context)
        val adapter = HomeworkAdapter(mutableListOf(), showActions = false)
        recyclerView.adapter = adapter

        // Simplification for the rudimentary version: show all for now or filter by a dummy user
        viewModel.allHomework.observe(viewLifecycleOwner) { homeworks ->
            // Filter logic can be added here
            adapter.updateData(homeworks)
        }

        btnPosted.setOnClickListener {
            // Show only posted by user
        }

        btnSolved.setOnClickListener {
            // Show only solved by user
        }

        return view
    }
}