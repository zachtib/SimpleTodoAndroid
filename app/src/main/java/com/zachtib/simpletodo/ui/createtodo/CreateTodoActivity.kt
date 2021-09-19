package com.zachtib.simpletodo.ui.createtodo

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.zachtib.simpletodo.R
import com.zachtib.simpletodo.data.SimpleTodoDatabase

class CreateTodoActivity : AppCompatActivity() {

    // A delegate call, just like in TodoListActivity
    private val viewModel by viewModels<CreateTodoViewModel> {

        // Get or create our database
        val database = SimpleTodoDatabase.getInstance(this)

        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                // Suppressing a warning, this object will only ever be used to
                // create CreateTodoViewModels
                @Suppress("UNCHECKED_CAST")
                return CreateTodoViewModel(database.todoItemDao()) as T
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_todo)

        // As usual, get all of our UI elements with findViewById
        val titleEditText = findViewById<EditText>(R.id.titleEditText)
        val descriptionEditText = findViewById<EditText>(R.id.descriptionEditText)
        val saveButton = findViewById<Button>(R.id.saveButton)

        // We observe this value so the viewModel can disable the button
        // (ie, while saving is in progress)
        viewModel.saveButtonEnabled.observe(this) { saveButtonEnabled ->
            saveButton.isEnabled = saveButtonEnabled
        }

        // Bind the two EditText fields and save button to our ViewModel
        titleEditText.doAfterTextChanged { text ->
            viewModel.onTitleChanged(text?.toString() ?: "")
        }
        descriptionEditText.doAfterTextChanged { text ->
            viewModel.onDescriptionChanged(text?.toString() ?: "")
        }
        saveButton.setOnClickListener {
            viewModel.onSavePressed()
        }

        // Observe this value to know when saving is complete.
        viewModel.saveComplete.observe(this) { saveWasSuccessful ->
            // Check that the save was successful, and if so, close the screen
            if (saveWasSuccessful) {
                finish()
            }
        }
    }
}