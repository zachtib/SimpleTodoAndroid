package com.zachtib.simpletodo.data

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Singleton
    @Provides
    fun provideSimpleTodoDatabase(
        @ApplicationContext context: Context,
    ): SimpleTodoDatabase {
        return Room.databaseBuilder(
            context, SimpleTodoDatabase::class.java, "todo-db"
        ).build()
    }

    @Provides
    fun provideTodoItemDao(db: SimpleTodoDatabase): TodoItemDao {
        return db.todoItemDao()
    }
}