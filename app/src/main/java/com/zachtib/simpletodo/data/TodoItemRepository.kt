package com.zachtib.simpletodo.data

import com.zachtib.simpletodo.models.TodoItem
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class TodoItemRepository @Inject constructor(
    private val todoItemDao: TodoItemDao,
) {
    fun loadTodoItems(): Flow<List<TodoItem>> {
        return todoItemDao.loadTodoItems()
    }

    suspend fun getTodoItem(itemId: Int): TodoItem {
        return todoItemDao.getTodoItem(itemId)
    }

    suspend fun createTodoItem(title: String, description: String = "") {
        val newTodoItem = TodoItem(0, title, false, description)
        todoItemDao.insert(newTodoItem)
    }

    suspend fun update(todoItem: TodoItem) {
        todoItemDao.update(todoItem)
    }

    suspend fun delete(todoItem: TodoItem) {
        todoItemDao.delete(todoItem)
    }
}