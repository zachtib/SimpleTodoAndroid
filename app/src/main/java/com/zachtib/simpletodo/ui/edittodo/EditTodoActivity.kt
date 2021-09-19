package com.zachtib.simpletodo.ui.edittodo

import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
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

    // Just a variable to store the name of the loaded item,
    // for the purpose of displaying it in the delete dialog
    // later on
    private var todoItemName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_todo)

        if (itemId == -1) {
            // No itemId was set, so there's no way for us
            // to load anything to edit, so exit.
            Log.w(
                "EditTodoActivity",
                "EditTodoActivity was launched without setting EDIT_TODO_ITEM_ID"
            )
            finish()
        }

        // As usual, get all of our UI elements with findViewById
        val editTodoItemHeader: TextView = findViewById(R.id.editTodoItemHeader)
        val titleEditText: EditText = findViewById(R.id.titleEditText)
        val todoItemComplete: CheckBox = findViewById(R.id.todoItemComplete)
        val descriptionEditText: EditText = findViewById(R.id.descriptionEditText)
        val deleteButton: Button = findViewById(R.id.deleteButton)
        val saveButton: Button = findViewById(R.id.saveButton)

        // We'll wait for the viewModel to finishing loading the item from the database
        // before we populate the data into our views (this will be more or less instant,
        // we just can't count on it being immediate.
        viewModel.editingTodoItem.observe(this) { todoItem ->
            editTodoItemHeader.text = getString(R.string.editing_todo, todoItem.title)
            titleEditText.setText(todoItem.title)
            todoItemComplete.isChecked = todoItem.isComplete
            descriptionEditText.setText(todoItem.description)
            todoItemName = todoItem.title
        }

        // Now we'll wire up responding to changes in state from the viewModel

        viewModel.deleteButtonEnabled.observe(this) { enabled ->
            deleteButton.isEnabled = enabled
        }

        viewModel.saveButtonEnabled.observe(this) { enabled ->
            saveButton.isEnabled = enabled
        }

        // As well as passing our events into the viewModel

        titleEditText.doAfterTextChanged { text ->
            viewModel.onTitleChanged(text?.toString() ?: "")
        }

        todoItemComplete.setOnCheckedChangeListener { _, isChecked ->
            viewModel.onCompleteChanged(isChecked)
        }

        descriptionEditText.doAfterTextChanged { text ->
            viewModel.onDescriptionChanged(text?.toString() ?: "")
        }

        deleteButton.setOnClickListener {
            // We'll show a confirmation dialog before deleting.
            showDeleteConfirmationDialog()
        }

        saveButton.setOnClickListener {
            viewModel.onSavePressed()
        }

        viewModel.editingComplete.observe(this) { value ->
            // Get the name (or fallback) we'll use for our Toast
            val name = todoItemName ?: getString(R.string.item_name_fallback)

            // Determine which message we're going to show
            if (value == EditTodoViewModel.ITEM_SAVED) {
                Toast.makeText(
                    this,
                    getString(R.string.item_updated, name),
                    Toast.LENGTH_SHORT
                ).show()
            } else if (value == EditTodoViewModel.ITEM_DELETED) {
                Toast.makeText(
                    this,
                    getString(R.string.item_was_deleted, name),
                    Toast.LENGTH_SHORT
                ).show()
            }

            // Then, finish the activity
            finish()
        }
    }

    // Shows a confirmation dialog, then calls the delete method on the viewModel if
    // the user selects 'OK'
    private fun showDeleteConfirmationDialog() {
        val name = todoItemName ?: getString(R.string.delete_name_fallback)

        AlertDialog.Builder(this)
            .setTitle(R.string.confirm_delete)
            .setMessage(getString(R.string.confirm_delete_msg, name))
            .setPositiveButton(R.string.confirm) { _, _ ->
                viewModel.onDeleteConfirmed()
            }
            .setNegativeButton(R.string.cancel) { _, _ ->
                Log.d("EditTodoActivity", "User cancelled deletion")
            }
            .create()
            .show()
    }
}