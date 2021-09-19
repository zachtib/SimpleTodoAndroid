package com.zachtib.simpletodo.ui.edittodo

import android.util.Log
import androidx.lifecycle.*
import com.zachtib.simpletodo.data.TodoItemDao
import com.zachtib.simpletodo.models.TodoItem
import kotlinx.coroutines.launch
import java.lang.Exception

class EditTodoViewModel(
    private val editingTodoItemId: Int,
    private val dao: TodoItemDao,
) : ViewModel() {

    companion object {
        // We'll use these constants to indicate the result of our editing.
        const val ITEM_SAVED = 1
        const val ITEM_DELETED = 2
    }

    // Our private, mutable state
    private val mutableSaveButtonEnabled = MutableLiveData(false)
    private val mutableDeleteButtonEnabled = MutableLiveData(false)
    private val mutableEditingComplete = MutableLiveData<Int>()
    private var todoItemTitle: String = ""
    private var todoItemComplete: Boolean = false
    private var todoItemDescription: String = ""

    // Publicly accessible state our activity will use
    val saveButtonEnabled: LiveData<Boolean> = mutableSaveButtonEnabled
    val deleteButtonEnabled: LiveData<Boolean> = mutableDeleteButtonEnabled
    val editingComplete: LiveData<Int> = mutableEditingComplete

    // We'll use the liveData builder in order to grab the underlying
    // TodoItem. Since this is an editing screen, we don't really care
    // all that much about observing changes to it, we just want to fetch
    // it once to fill in the values on the form.
    val editingTodoItem: LiveData<TodoItem> = liveData {
        val todoItem = dao.getTodoItem(editingTodoItemId)
        emit(todoItem)
        // We'll also enable the delete button once loading is complete.
        mutableDeleteButtonEnabled.value = true
    }

    fun onTitleChanged(newValue: String) {
        todoItemTitle = newValue

        // We only want to allow saving if the user has entered some text for title
        mutableSaveButtonEnabled.value = todoItemTitle.isNotBlank()
    }

    fun onCompleteChanged(newValue: Boolean) {
        todoItemComplete = newValue
    }

    fun onDescriptionChanged(newValue: String) {
        todoItemDescription = newValue
    }

    fun onDeleteConfirmed() {
        // Grab the loaded item, or return early if we don't have one, somehow
        val todoItem = editingTodoItem.value ?: return

        // Disable our save/delete buttons
        mutableSaveButtonEnabled.value = false
        mutableDeleteButtonEnabled.value = false

        viewModelScope.launch {
            try {
                dao.delete(todoItem)
                mutableEditingComplete.value = ITEM_DELETED
            } catch (e: Exception) {
                Log.e("EditTodoViewModel", "Error deleting TodoItem", e)
                mutableSaveButtonEnabled.value = true
                mutableDeleteButtonEnabled.value = true
            }
        }
    }

    fun onSavePressed() {
        // Grab the loaded item, or return early if we don't have one, somehow
        val todoItem = editingTodoItem.value ?: return

        // Update the todoItem with the entered values
        val updatedTodoItem = todoItem.copy(
            title = todoItemTitle,
            isComplete = todoItemComplete,
            description = todoItemDescription
        )

        mutableSaveButtonEnabled.value = false
        mutableDeleteButtonEnabled.value = false

        viewModelScope.launch {
            try {
                dao.update(updatedTodoItem)
                mutableEditingComplete.value = ITEM_SAVED
            } catch (e: Exception) {
                Log.e("EditTodoViewModel", "Error saving TodoItem", e)
                mutableSaveButtonEnabled.value = true
                mutableDeleteButtonEnabled.value = true
            }
        }
    }
}