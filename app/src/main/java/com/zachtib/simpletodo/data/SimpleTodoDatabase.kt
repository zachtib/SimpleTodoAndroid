package com.zachtib.simpletodo.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.zachtib.simpletodo.models.TodoItem

@Database(
    entities = [TodoItem::class],
    version = 1,
    exportSchema = false,
)
abstract class SimpleTodoDatabase : RoomDatabase() {

    abstract fun todoItemDao(): TodoItemDao
}