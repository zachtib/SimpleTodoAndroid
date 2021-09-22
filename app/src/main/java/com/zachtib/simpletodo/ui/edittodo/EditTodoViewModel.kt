package com.zachtib.simpletodo.ui.edittodo

import androidx.lifecycle.*
import com.zachtib.simpletodo.data.TodoItemRepository
import com.zachtib.simpletodo.models.TodoItem
import com.zachtib.simpletodo.ui.TodoItemId
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class EditTodoViewModel @Inject constructor(
    private val repository: TodoItemRepository,
    @TodoItemId private val editingTodoItemId: Int,
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
        val todoItem = repository.getTodoItem(editingTodoItemId)
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
                repository.delete(todoItem)
                mutableEditingComplete.value = ITEM_DELETED
            } catch (e: Exception) {
                Timber.e(e, "Error deleting TodoItem")
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
                repository.update(updatedTodoItem)
                mutableEditingComplete.value = ITEM_SAVED
            } catch (e: Exception) {
                Timber.e(e, "Error saving TodoItem")
                mutableSaveButtonEnabled.value = true
                mutableDeleteButtonEnabled.value = true
            }
        }
    }
}