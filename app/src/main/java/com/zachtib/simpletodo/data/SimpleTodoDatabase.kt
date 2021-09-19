package com.zachtib.simpletodo.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.zachtib.simpletodo.models.TodoItem

@Database(
    entities = [TodoItem::class],
    version = 1,
    exportSchema = false,
)
abstract class SimpleTodoDatabase : RoomDatabase() {

    abstract fun todoItemDao(): TodoItemDao

    companion object {
        // Singleton pattern for building the database. Room databases are expensive in
        // terms of resources, so we want to make sure we only create one. The public
        // method, getInstance, will return this instance value if it is set, otherwise
        // it will call createInstance to perform the database initialization.
        private var instance: SimpleTodoDatabase? = null

        private fun createInstance(context: Context): SimpleTodoDatabase {
            val newInstance = Room.databaseBuilder(
                context, SimpleTodoDatabase::class.java, "todo-db"
            ).build()
            instance = newInstance
            return newInstance
        }

        fun getInstance(context: Context): SimpleTodoDatabase {
            return instance ?: createInstance(context)
        }
    }
}