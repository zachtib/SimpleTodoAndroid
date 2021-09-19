package com.zachtib.simpletodo.ui.createtodo

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
        setContentView(R.layout.activity_createedit_todo)

        // As usual, get all of our UI elements with findViewById
        val editTodoItemHeader = findViewById<TextView>(R.id.editTodoItemHeader)
        val titleEditText = findViewById<EditText>(R.id.titleEditText)
        val descriptionEditText = findViewById<EditText>(R.id.descriptionEditText)
        val saveButton = findViewById<Button>(R.id.saveButton)

        // Set the header, because it's not set in the layout XML
        editTodoItemHeader.text = getString(R.string.add_a_todo)

        // We observe this value so the viewModel can disable the button
        // (ie, while saving is in progress)
        viewModel.saveButtonEnabled.observe(this) { saveButtonEnabled ->
            saveButton.isEnabled = saveButtonEnabled
        }

        // Bind the two EditText fields and save button to our ViewModel
        titleEditText.doAfterTextChanged(viewModel::onTitleChanged)
        descriptionEditText.doAfterTextChanged(viewModel::onDescriptionChanged)
        saveButton.setOnClickListener(viewModel::onSavePressed)

        // Observe this value to know when saving is complete.
        viewModel.saveComplete.observe(this) { saveWasSuccessful ->
            if (saveWasSuccessful) {
                // Check that the save was successful, and if so, close the screen
                finish()
            }
        }
    }
}