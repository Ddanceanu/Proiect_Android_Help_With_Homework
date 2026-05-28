package com.example.proiecthelpwithhomework

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class HomeworkViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = AppDatabase.getDatabase(application).homeworkDao()
    
    private val _searchQuery = MutableStateFlow("")
    val searchQuery get() = _searchQuery
    
    val allHomework: LiveData<List<Homework>> = _searchQuery.flatMapLatest { query ->
        if (query.isEmpty()) {
            dao.getAllHomework()
        } else {
            dao.searchHomework("%$query%")
        }
    }.asLiveData(Dispatchers.IO)

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun insert(homework: Homework) = viewModelScope.launch(Dispatchers.IO) {
        dao.insert(homework)
    }

    fun update(homework: Homework) = viewModelScope.launch(Dispatchers.IO) {
        dao.update(homework)
    }

    fun delete(homework: Homework) = viewModelScope.launch(Dispatchers.IO) {
        dao.delete(homework)
    }
}