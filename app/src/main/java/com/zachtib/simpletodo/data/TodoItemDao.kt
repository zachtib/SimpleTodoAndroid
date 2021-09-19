package com.zachtib.simpletodo.data

import androidx.room.*
import com.zachtib.simpletodo.models.TodoItem
import kotlinx.coroutines.flow.Flow


/**
 * Our Data Access Object. Room will create an implementation of this class when our
 * app is run, that implements the functions below.
 *
 * The functions that return a Flow will watch the database for updates and emit new
 * values as the underlying values change.
 */
@Dao
interface TodoItemDao {

    @Query("SELECT * FROM TodoItem")
    fun loadTodoItems(): Flow<List<TodoItem>>

    @Query("SELECT * FROM TodoItem WHERE id=:itemId LIMIT 1")
    fun loadTodoItem(itemId: Int): Flow<TodoItem>

    @Query("SELECT * FROM TodoItem WHERE id=:itemId LIMIT 1")
    suspend fun getTodoItem(itemId: Int): TodoItem

    @Insert
    suspend fun insert(todoItem: TodoItem)

    @Update
    suspend fun update(todoItem: TodoItem)

    @Delete
    suspend fun delete(todoItem: TodoItem)
}