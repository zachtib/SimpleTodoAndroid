package com.zachtib.simpletodo.ui.edittodo

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.zachtib.simpletodo.R
import com.zachtib.simpletodo.databinding.EditTodoFragmentBinding
import com.zachtib.simpletodo.ui.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber


@AndroidEntryPoint
class EditTodoFragment : Fragment(R.layout.edit_todo_fragment) {

    // Our ViewModel and ViewBinding delegates
    private val viewModel by viewModels<EditTodoViewModel>()
    private val binding by viewBinding(EditTodoFragmentBinding::bind)

    // Just a variable to store the name of the loaded item,
    // for the purpose of displaying it in the delete dialog
    // later on
    private var todoItemName: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // We don't need to worry about itemId not being set anymore, because the
        // Navigation SafeArgs ensures that it is there.

        with(binding) {
            // We'll wait for the viewModel to finishing loading the item from the database
            // before we populate the data into our views (this will be more or less instant,
            // we just can't count on it being immediate.
            viewModel.editingTodoItem.observe(viewLifecycleOwner) { todoItem ->
                editTodoItemHeader.text = getString(R.string.editing_todo, todoItem.title)
                titleEditText.setText(todoItem.title)
                todoItemComplete.isChecked = todoItem.isComplete
                descriptionEditText.setText(todoItem.description)
                todoItemName = todoItem.title
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

            // Now we'll wire up responding to changes in state from the viewModel
            viewModel.deleteButtonEnabled.observe(viewLifecycleOwner) { enabled ->
                deleteButton.isEnabled = enabled
            }

            viewModel.saveButtonEnabled.observe(viewLifecycleOwner) { enabled ->
                saveButton.isEnabled = enabled
            }

            viewModel.editingComplete.observe(viewLifecycleOwner) { value ->
                // Get the name (or fallback) we'll use for our Toast
                val name = todoItemName ?: getString(R.string.item_name_fallback)

                // Determine which message we're going to show
                if (value == EditTodoViewModel.ITEM_SAVED) {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.item_updated, name),
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (value == EditTodoViewModel.ITEM_DELETED) {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.item_was_deleted, name),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                // Then, pop this screen off the stack
                findNavController().navigateUp()
            }
        }
    }

    // Shows a confirmation dialog, then calls the delete method on the viewModel if
    // the user selects 'OK'
    private fun showDeleteConfirmationDialog() {
        val name = todoItemName ?: getString(R.string.delete_name_fallback)

        AlertDialog.Builder(requireContext())
            .setTitle(R.string.confirm_delete)
            .setMessage(getString(R.string.confirm_delete_msg, name))
            .setPositiveButton(R.string.confirm) { _, _ ->
                viewModel.onDeleteConfirmed()
            }
            .setNegativeButton(R.string.cancel) { _, _ ->
                Timber.d("User cancelled deletion")
            }
            .create()
            .show()
    }
}