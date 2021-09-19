package com.zachtib.simpletodo.ui.edittodo

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.zachtib.simpletodo.R
import com.zachtib.simpletodo.data.SimpleTodoDatabase


class EditTodoActivity : AppCompatActivity() {

    companion object {
        // A constant we'll use when launching this screen
        const val EDIT_TODO_ITEM_ID = "EDIT_TODO_ITEM_ID"
    }

    // Get the id of the item we're editing, or -1 if it was not set.
    private val itemId: Int
        get() = intent.getIntExtra(EDIT_TODO_ITEM_ID, -1)

    // A delegate call, just like in TodoListActivity
    private val viewModel by viewModels<EditTodoViewModel> {
        val database = SimpleTodoDatabase.getInstance(this)

        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                // Suppressing a warning, this object will only ever be used to
                // create EditTodoViewModels
                @Suppress("UNCHECKED_CAST")
                return EditTodoViewModel(itemId, database.todoItemDao()) as T
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_todo)

        if (itemId == -1) {
            // No itemId was set, so there's no way for us
            // to load anything to edit, so exit.
            Log.w("EditTodoActivity",
                "EditTodoActivity was launched without setting EDIT_TODO_ITEM_ID")
            finish()
        }

        // As usual, get all of our UI elements with findViewById
        val editTodoItemHeader = findViewById<TextView>(R.id.editTodoItemHeader)
        val titleEditText = findViewById<EditText>(R.id.titleEditText)
        val todoItemComplete = findViewById<CheckBox>(R.id.todoItemComplete)
        val descriptionEditText = findViewById<EditText>(R.id.descriptionEditText)
        val saveButton = findViewById<Button>(R.id.saveButton)

        viewModel.editingTodoItem.observe(this) { todoItem ->
            editTodoItemHeader.text = getString(R.string.editing_todo, todoItem.title)
            titleEditText.setText(todoItem.title)
            todoItemComplete.isChecked = todoItem.isComplete
            descriptionEditText.setText(todoItem.description)
            saveButton.isEnabled = true
        }

        saveButton.setOnClickListener {
            viewModel.onSavePressed()
        }
    }
}