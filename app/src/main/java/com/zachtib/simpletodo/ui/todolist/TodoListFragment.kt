package com.zachtib.simpletodo.ui.todolist

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.zachtib.simpletodo.R
import com.zachtib.simpletodo.databinding.TodoListFragmentBinding
import com.zachtib.simpletodo.models.TodoItem
import com.zachtib.simpletodo.ui.todolistadapter.TodoListAdapter
import com.zachtib.simpletodo.ui.viewBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class TodoListFragment : Fragment(R.layout.todo_list_fragment) {

    // A delegate call to either create, or grab our existing viewModel
    private val viewModel by viewModels<TodoListViewModel>()

    // A delegate call to get our ViewBinding for this Fragment
    private val binding by viewBinding(TodoListFragmentBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // First, we'll create an instance of our list adapter, passing in callbacks
        // for itemClicked and itemChecked events. The click events will be handled
        // by this Activity, because they will result in a navigation event, but the
        // check events will be passed to the ViewModel, as it needs to update the
        // underlying data.
        val todoListAdapter = TodoListAdapter(
            itemClickCallback = this::navigateToEditTodo,
            itemCheckedChangeCallback = viewModel::onItemChecked,
        )

        // Using a scoping function here just so we don't have to type `binding.` every time
        with(binding) {
            // Now we'll set a linear layout for the recycler, and attach the adapter
            todoListRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            todoListRecyclerView.adapter = todoListAdapter

            // Now we'll observe our viewModel for changes to the data
            viewModel.todoItems.observe(viewLifecycleOwner) { todoItems ->
                if (todoItems.isEmpty()) {
                    // Handle a special case where there are no todoItems
                    todoListRecyclerView.isVisible = false
                    emptyMessage.isVisible = true
                } else {
                    // Set visibility here, in case the empty list case was handled previously
                    todoListRecyclerView.isVisible = true
                    emptyMessage.isVisible = false

                    // Submit the list of items to our list adapter
                    todoListAdapter.submitList(todoItems)
                }
            }

            // Bind the addTodoButton's click event to go to the create todoItem screen
            addTodoButton.setOnClickListener {
                navigateToCreateTodo()
            }
        }
    }

    private fun navigateToCreateTodo() {
        val action = TodoListFragmentDirections.navigateToCreateTodo()
        findNavController().navigate(action)
    }

    private fun navigateToEditTodo(todoItem: TodoItem) {
        val action = TodoListFragmentDirections.navigateToEditTodo(todoItem.id)
        findNavController().navigate(action)
    }
}