package com.example.proiecthelpwithhomework

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.ChipGroup

class HomeworkListFragment : Fragment() {

    private val viewModel: HomeworkViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_homework_list, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        val searchEditText = view.findViewById<EditText>(R.id.searchEditText)
        val sortButton = view.findViewById<ImageButton>(R.id.sortButton)
        val filterChipGroup = view.findViewById<ChipGroup>(R.id.filterChipGroup)
        
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
            showSortMenu(it)
        }
        
        filterChipGroup.setOnCheckedChangeListener { _, checkedId ->
            val filter = when (checkedId) {
                R.id.chipSolved -> FilterStatus.SOLVED
                R.id.chipUnsolved -> FilterStatus.UNSOLVED
                else -> FilterStatus.ALL
            }
            viewModel.setFilterStatus(filter)
        }

        return view
    }

    private fun showSortMenu(view: View) {
        val popup = PopupMenu(requireContext(), view)
        popup.menu.add(0, 1, 0, "După titlu (A-Z)")
        popup.menu.add(0, 2, 1, "Cele mai noi")
        popup.menu.add(0, 3, 2, "Stare (Rezolvate/Nerezolvate)")
        
        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                1 -> viewModel.setSortType(SortType.TITLE)
                2 -> viewModel.setSortType(SortType.ID)
                3 -> viewModel.setSortType(SortType.STATUS)
            }
            true
        }
        popup.show()
    }
}
