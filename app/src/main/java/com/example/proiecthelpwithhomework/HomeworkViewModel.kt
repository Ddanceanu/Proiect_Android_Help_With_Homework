package com.example.proiecthelpwithhomework

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

enum class SortType { TITLE, ID, STATUS }
enum class FilterStatus { ALL, SOLVED, UNSOLVED }

@OptIn(ExperimentalCoroutinesApi::class)
class HomeworkViewModel(application: Application) : AndroidViewModel(application) {
    private val db = AppDatabase.getDatabase(application)
    private val homeworkDao = db.homeworkDao()
    private val commentDao = db.commentDao()
    
    private val _searchQuery = MutableStateFlow("")
    private val _sortType = MutableStateFlow(SortType.ID)
    private val _filterStatus = MutableStateFlow(FilterStatus.ALL)
    
    val allHomework: LiveData<List<Homework>> = combine(
        _searchQuery,
        _sortType,
        _filterStatus
    ) { query, sort, filter ->
        Triple(query, sort, filter)
    }.flatMapLatest { (query, sort, filter) ->
        val flow = if (query.isEmpty()) {
            homeworkDao.getAllHomework()
        } else {
            homeworkDao.searchHomework("%$query%")
        }
        
        flow.combine(MutableStateFlow(Unit)) { list, _ ->
            var filteredList = when (filter) {
                FilterStatus.ALL -> list
                FilterStatus.SOLVED -> list.filter { it.isSolved }
                FilterStatus.UNSOLVED -> list.filter { !it.isSolved }
            }
            
            when (sort) {
                SortType.TITLE -> filteredList.sortedBy { it.title }
                SortType.ID -> filteredList.sortedByDescending { it.id }
                SortType.STATUS -> filteredList.sortedBy { it.isSolved } // Unsolved first usually, or reverse
            }
        }
    }.asLiveData(Dispatchers.IO)

    // User activity logic
    private val _userFilter = MutableLiveData<Pair<String, Boolean>>()

    val userActivityHomework: LiveData<List<Homework>> = _userFilter.switchMap { filter ->
        if (filter.second) {
            homeworkDao.getSolvedHomeworkByUser(filter.first).asLiveData(Dispatchers.IO)
        } else {
            homeworkDao.getHomeworkByUser(filter.first).asLiveData(Dispatchers.IO)
        }
    }

    fun setActivityFilter(userName: String, isSolved: Boolean) {
        _userFilter.value = Pair(userName, isSolved)
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }
    
    fun setSortType(sortType: SortType) {
        _sortType.value = sortType
    }
    
    fun setFilterStatus(filterStatus: FilterStatus) {
        _filterStatus.value = filterStatus
    }

    fun getHomeworkById(id: Int): LiveData<Homework?> {
        return homeworkDao.getHomeworkById(id).asLiveData(Dispatchers.IO)
    }

    fun getCommentsForHomework(homeworkId: Int): LiveData<List<Comment>> {
        return commentDao.getCommentsForHomework(homeworkId).asLiveData(Dispatchers.IO)
    }

    fun insert(homework: Homework) = viewModelScope.launch(Dispatchers.IO) {
        homeworkDao.insert(homework)
    }

    fun update(homework: Homework) = viewModelScope.launch(Dispatchers.IO) {
        homeworkDao.update(homework)
    }

    fun delete(homework: Homework) = viewModelScope.launch(Dispatchers.IO) {
        homeworkDao.delete(homework)
    }

    fun insertComment(comment: Comment) = viewModelScope.launch(Dispatchers.IO) {
        commentDao.insert(comment)
    }

    fun voteComment(commentId: Int, userName: String, voteType: Int) = viewModelScope.launch(Dispatchers.IO) {
        val existingVote = commentDao.getVote(commentId, userName)
        val comment = commentDao.getCommentById(commentId) ?: return@launch

        if (existingVote == null) {
            commentDao.insertVote(CommentVote(commentId, userName, voteType))
            if (voteType == 1) comment.likes++ else comment.dislikes++
        } else if (existingVote.voteType == voteType) {
            commentDao.deleteVote(commentId, userName)
            if (voteType == 1) comment.likes-- else comment.dislikes--
        } else {
            commentDao.insertVote(CommentVote(commentId, userName, voteType))
            if (voteType == 1) {
                comment.likes++
                comment.dislikes--
            } else {
                comment.likes--
                comment.dislikes++
            }
        }
        commentDao.update(comment)
    }
}
