package com.zachtib.simpletodo.ui.todolist

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.zachtib.simpletodo.data.TodoItemRepository
import com.zachtib.simpletodo.models.TodoItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class TodoListViewModel @Inject constructor(
    private val repository: TodoItemRepository
) : ViewModel() {

    val todoItems: LiveData<List<TodoItem>> = liveData {
        repository.loadTodoItems().collect { items ->
            emit(items)
        }
    }

    fun onItemChecked(todoItem: TodoItem, isChecked: Boolean) {
        // Copy the TodoItem, but with an updated isComplete value
        val updatedItem = todoItem.copy(isComplete = isChecked)

        viewModelScope.launch {
            // Then store the item in the database.
            repository.update(updatedItem)
        }
    }
}