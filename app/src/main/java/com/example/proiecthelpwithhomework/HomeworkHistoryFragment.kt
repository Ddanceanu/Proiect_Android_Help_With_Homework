package com.example.proiecthelpwithhomework

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.feature.auth.data.session.SessionManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class HomeworkHistoryFragment : Fragment() {

    private val viewModel: HomeworkViewModel by activityViewModels()
    private lateinit var sessionManager: SessionManager
    private var currentUserName: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_homework_history, container, false)

        sessionManager = SessionManager(requireContext())

        val recyclerView = view.findViewById<RecyclerView>(R.id.historyRecyclerView)
        val btnPosted = view.findViewById<Button>(R.id.btnPosted)
        val btnSolved = view.findViewById<Button>(R.id.btnSolved)

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
                    putExtra("postedBy", homework.postedBy)
                    putExtra("isSolved", homework.isSolved)
                    homework.acceptedCommentId?.let { putExtra("acceptedId", it) }
                }
                startActivity(intent)
            }
        )
        recyclerView.adapter = adapter

        // Single observer for user activity
        viewModel.userActivityHomework.observe(viewLifecycleOwner) { homeworks ->
            adapter.updateData(homeworks)
        }

        lifecycleScope.launch {
            currentUserName = sessionManager.userNameFlow.first()
            currentUserName?.let {
                // Initial load
                viewModel.setActivityFilter(it, false)
            }
        }

        btnPosted.setOnClickListener {
            currentUserName?.let { viewModel.setActivityFilter(it, false) }
        }

        btnSolved.setOnClickListener {
            currentUserName?.let { viewModel.setActivityFilter(it, true) }
        }

        return view
    }
}