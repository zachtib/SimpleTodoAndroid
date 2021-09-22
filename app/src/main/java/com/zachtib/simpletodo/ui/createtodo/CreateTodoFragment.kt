package com.zachtib.simpletodo.ui.createtodo

import android.os.Bundle
import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.zachtib.simpletodo.R
import com.zachtib.simpletodo.databinding.CreateTodoFragmentBinding
import com.zachtib.simpletodo.ui.viewBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CreateTodoFragment : Fragment(R.layout.create_todo_fragment) {

    // Delegates calls for viewModel and binding, just like in TodoListFragment
    private val viewModel by viewModels<CreateTodoViewModel>()
    private val binding by viewBinding(CreateTodoFragmentBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
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

            // We observe this value so the viewModel can disable the button
            // (ie, while saving is in progress)
            viewModel.saveButtonEnabled.observe(viewLifecycleOwner) { saveButtonEnabled ->
                saveButton.isEnabled = saveButtonEnabled
            }
        }

        // Observe this value to know when saving is complete.
        viewModel.saveComplete.observe(viewLifecycleOwner) { saveWasSuccessful ->
            // Check that the save was successful, and if so, close the screen
            if (saveWasSuccessful) {
                findNavController().navigateUp()
            }
        }
    }
}