package com.zachtib.simpletodo.ui.edittodo

import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.zachtib.simpletodo.R


class EditTodoActivity : AppCompatActivity() {

    companion object {
        // A constant we'll use when launching this screen
        const val EDIT_TODO_ITEM_ID = "EDIT_TODO_ITEM_ID"
    }

    // Get the id of the item we're editing, or default to -1 if it was not set.
    private val itemId: Int = intent.getIntExtra(EDIT_TODO_ITEM_ID, -1)

    // A delegate call, just like in TodoListActivity
    private val viewModel by viewModels<EditTodoViewModel> {
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
        setContentView(R.layout.activity_createedit_todo)

        // As usual, get all of our UI elements with findViewById
        val editTodoItemHeader = findViewById<TextView>(R.id.editTodoItemHeader)
        val titleEditText = findViewById<EditText>(R.id.titleEditText)
        val todoItemComplete = findViewById<CheckBox>(R.id.todoItemComplete)
        val descriptionEditText = findViewById<EditText>(R.id.descriptionEditText)
        val saveButton = findViewById<Button>(R.id.saveButton)
    }
}