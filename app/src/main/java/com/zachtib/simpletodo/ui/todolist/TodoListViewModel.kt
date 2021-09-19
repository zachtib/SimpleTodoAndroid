package com.zachtib.simpletodo.ui.todolist

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.zachtib.simpletodo.data.TodoItemDao
import com.zachtib.simpletodo.models.TodoItem
import kotlinx.coroutines.flow.collect

class TodoListViewModel(
    private val dao: TodoItemDao
) : ViewModel() {
    val todoItems: LiveData<List<TodoItem>> = liveData {
        dao.loadTodoItems().collect { items ->
            emit(items)
        }
    }
}