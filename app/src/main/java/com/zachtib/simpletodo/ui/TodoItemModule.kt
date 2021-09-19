package com.zachtib.simpletodo.ui

import androidx.lifecycle.SavedStateHandle
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class TodoItemId

@Module
@InstallIn(ViewModelComponent::class)
class TodoItemModule {

    // Provides an Int todoItemId to ViewModels from the navigation arguments

    @Provides
    @TodoItemId
    fun provideTodoItemId(state: SavedStateHandle): Int {
        return state.get<Int>("todoItemId")
            ?: throw IllegalStateException("todoItemId is required, but was not a part of the navigation directions")
    }
}