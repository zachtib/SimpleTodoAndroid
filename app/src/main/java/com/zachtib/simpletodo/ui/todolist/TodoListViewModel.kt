package com.zachtib.simpletodo.ui.todolist

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.zachtib.simpletodo.data.TodoItemDao
import com.zachtib.simpletodo.models.TodoItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class TodoListViewModel @Inject constructor(
    private val dao: TodoItemDao
) : ViewModel() {

    val todoItems: LiveData<List<TodoItem>> = liveData {
        dao.loadTodoItems().collect { items ->
            emit(items)
        }
    }

    fun onItemChecked(todoItem: TodoItem, isChecked: Boolean) {
        // Copy the TodoItem, but with an updated isComplete value
        val updatedItem = todoItem.copy(isComplete = isChecked)

        viewModelScope.launch {
            // Then store the item in the database.
            dao.update(updatedItem)
        }
    }
}