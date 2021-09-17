package com.zachtib.simpletodo.ui.todolist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.zachtib.simpletodo.R
import com.zachtib.simpletodo.ui.edittodo.EditTodoActivity

class TodoListActivity : AppCompatActivity() {

    // A delegate call to either create, or grab our existing viewmodel
    private val viewModel by viewModels<TodoListViewModel> {

        // This function expects a ViewModel Factory, or something that will be used
        // to create the ViewModel the first time we need it, so we will provide a
        // simple factory here. In a more robust app, this would probably be handled
        // by some sort of dependency injection framework.
        object : ViewModelProvider.Factory {

            // A ViewModelProvider.Factory just has one method, which creates the
            // ViewModel instance. Don't mind the Generic <T> parameters, ours will
            // only ever be building TodoListViewModels.
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {

                // Suppressing a warning, this object will only ever be used to
                // create TodoListViewModels
                @Suppress("UNCHECKED_CAST")
                return TodoListViewModel() as T
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo_list)

        // First, grab the UI elements that we care about
        val todoListRecyclerView = findViewById<RecyclerView>(R.id.todoListRecyclerView)
        val addTodoButton = findViewById<Button>(R.id.addTodoButton)
        val emptyMessage = findViewById<TextView>(R.id.emptyMessage)

        // Now, we want to configure our RecyclerView
    }

    private fun navigateToCreateTodo() {
        val launchIntent = Intent(this, EditTodoActivity::class.java)
        startActivity(launchIntent)
    }
}