package com.zachtib.simpletodo.ui.createtodo

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zachtib.simpletodo.data.TodoItemDao
import com.zachtib.simpletodo.models.TodoItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CreateTodoViewModel @Inject constructor(
    private val dao: TodoItemDao,
) : ViewModel() {

    // Our private, mutable state
    private val mutableSaveButtonEnabled = MutableLiveData(false)
    private val mutableSaveComplete = MutableLiveData(false)
    private var todoItemTitle: String = ""
    private var todoItemDescription: String = ""

    // Publicly accessible state our activity will use
    val saveButtonEnabled: LiveData<Boolean> = mutableSaveButtonEnabled
    val saveComplete: LiveData<Boolean> = mutableSaveComplete

    fun onTitleChanged(newValue: String) {
        todoItemTitle = newValue

        // We only want to allow saving if the user has entered some text for title
        mutableSaveButtonEnabled.value = todoItemTitle.isNotBlank()
    }

    fun onDescriptionChanged(newValue: String) {
        todoItemDescription = newValue
    }

    fun onSavePressed() {
        // Disable the save button while we are performing the save
        mutableSaveButtonEnabled.value = false
        viewModelScope.launch {
            try {
                val newTodoItem = TodoItem(
                    id = 0,
                    title = todoItemTitle,
                    isComplete = false,
                    description = todoItemDescription
                )
                dao.insert(newTodoItem)
                mutableSaveComplete.value = true
            } catch (e: Exception) {
                Log.e("CreateTodoViewModel", "Error creating TodoItem", e)
                // Something went wrong, re-enable the save button
                mutableSaveButtonEnabled.value = true
            }
        }
    }
}