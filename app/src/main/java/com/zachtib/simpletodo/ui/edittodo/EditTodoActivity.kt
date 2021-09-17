package com.zachtib.simpletodo.ui.edittodo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.zachtib.simpletodo.R
import com.zachtib.simpletodo.ui.todolist.TodoListViewModel


// A constant we'll use when launching this screen in "edit mode"
const val EDIT_TODO_ITEM_ID = "EDIT_TODO_ITEM_ID"

// We're going to use the same activity for both editing and creating Todos
class EditTodoActivity : AppCompatActivity() {

    // A delegate call, just like in TodoListActivity
    private val viewModel by viewModels<EditTodoViewModel> {

        // Get the id of the item we're editing, or default to -1 if it was not set.
        // We'll treat -1 in the ViewModel as us creating a new item, rather than editing.
        val itemId = intent.getIntExtra(EDIT_TODO_ITEM_ID, -1)

        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {

                // Suppressing a warning, this object will only ever be used to
                // create EditTodoViewModels
                @Suppress("UNCHECKED_CAST")
                return EditTodoViewModel(itemId) as T
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_todo)
    }
}