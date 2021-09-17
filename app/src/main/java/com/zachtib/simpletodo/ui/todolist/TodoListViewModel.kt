package com.zachtib.simpletodo.ui.todolist

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.zachtib.simpletodo.models.TodoItem

class TodoListViewModel : ViewModel() {
    val todoItems: LiveData<List<TodoItem>> = liveData {
        emit(listOf(
            TodoItem(1, "Set up Room", false, ""),
            TodoItem(2, "Put some data in it", false, ""),
            TodoItem(3, "Replace this dummy code", false, ""),
        ))
    }
}