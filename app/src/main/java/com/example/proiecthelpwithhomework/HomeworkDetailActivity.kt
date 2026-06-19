package com.example.proiecthelpwithhomework

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.feature.auth.data.session.SessionManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class HomeworkDetailActivity : AppCompatActivity() {

    private val viewModel: HomeworkViewModel by viewModels()
    private lateinit var sessionManager: SessionManager
    private var currentUserName: String? = null
    private var homeworkId: Int = -1
    private var adapter: CommentAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homework_detail)

        sessionManager = SessionManager(this)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { finish() }

        homeworkId = intent.getIntExtra("id", -1)
        
        val commentsRecyclerView = findViewById<RecyclerView>(R.id.commentsRecyclerView)
        val noSolutionsText = findViewById<TextView>(R.id.noSolutionsText)
        commentsRecyclerView.layoutManager = LinearLayoutManager(this)

        lifecycleScope.launch {
            currentUserName = sessionManager.userNameFlow.first()
            setupObservers(noSolutionsText, commentsRecyclerView)
        }

        findViewById<ImageButton>(R.id.saveSolutionButton).setOnClickListener {
            val input = findViewById<EditText>(R.id.solutionInput)
            val text = input.text.toString()
            if (text.isNotEmpty()) {
                lifecycleScope.launch {
                    val userName = sessionManager.userNameFlow.first() ?: "Anonim"
                    val comment = Comment(
                        homeworkId = homeworkId,
                        authorName = userName,
                        text = text
                    )
                    viewModel.insertComment(comment)
                    input.text.clear()
                }
            }
        }
    }

    private fun setupObservers(noSolutionsText: TextView, recyclerView: RecyclerView) {
        // Observe homework data (includes isSolved and acceptedCommentId)
        viewModel.getHomeworkById(homeworkId).observe(this) { homework ->
            if (homework == null) return@observe
            
            findViewById<TextView>(R.id.detailTitle).text = homework.title
            findViewById<TextView>(R.id.detailSubject).text = homework.subject
            findViewById<TextView>(R.id.detailDescription).text = homework.description
            findViewById<TextView>(R.id.postedByText).text = "Postat de: ${homework.postedBy}"

            if (adapter == null) {
                adapter = CommentAdapter(
                    emptyList(),
                    currentUserName = currentUserName,
                    postAuthorName = homework.postedBy,
                    acceptedCommentId = homework.acceptedCommentId,
                    onLike = { comment ->
                        currentUserName?.let { userName ->
                            viewModel.voteComment(comment.id, userName, 1)
                        } ?: run {
                            Toast.makeText(this, "Trebuie să fii logat!", Toast.LENGTH_SHORT).show()
                        }
                    },
                    onDislike = { comment ->
                        currentUserName?.let { userName ->
                            viewModel.voteComment(comment.id, userName, -1)
                        } ?: run {
                            Toast.makeText(this, "Trebuie să fii logat!", Toast.LENGTH_SHORT).show()
                        }
                    },
                    onAccept = { comment ->
                        acceptSolution(homework, comment)
                    }
                )
                recyclerView.adapter = adapter

                // Observe comments separately to avoid nesting emissions
                viewModel.getCommentsForHomework(homeworkId).observe(this) { comments ->
                    if (comments.isEmpty()) {
                        noSolutionsText.visibility = View.VISIBLE
                        recyclerView.visibility = View.GONE
                    } else {
                        noSolutionsText.visibility = View.GONE
                        recyclerView.visibility = View.VISIBLE
                        // Always get the latest acceptedId from the homework LiveData
                        val latestAcceptedId = viewModel.getHomeworkById(homeworkId).value?.acceptedCommentId
                        adapter?.updateData(comments, latestAcceptedId)
                    }
                }
            } else {
                // If homework object updated (e.g. solution accepted), refresh adapter state
                adapter?.updateData(adapter?.getList() ?: emptyList(), homework.acceptedCommentId)
            }
        }
    }

    private fun acceptSolution(homework: Homework, comment: Comment) {
        homework.isSolved = true
        homework.acceptedCommentId = comment.id
        homework.solvedBy = comment.authorName
        homework.solution = comment.text
        viewModel.update(homework)
    }
}
