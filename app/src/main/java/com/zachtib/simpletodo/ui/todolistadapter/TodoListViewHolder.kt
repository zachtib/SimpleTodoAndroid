package com.zachtib.simpletodo.ui.todolistadapter

import androidx.recyclerview.widget.RecyclerView
import com.zachtib.simpletodo.databinding.TodoListItemBinding
import com.zachtib.simpletodo.models.TodoItem


// Holds a single instance of our repeating list item, and is capable of being bound
// and re-bound to different TodoItem instances.
class TodoListViewHolder(
    private val binding: TodoListItemBinding,
    private val itemClickCallback: (TodoItem) -> Unit,
    private val itemCheckedChangeCallback: (TodoItem, Boolean) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {

    // Fills this view with the contents of todoItem
    fun bindTodoItem(todoItem: TodoItem) {
        with(binding) {
            checkBox.isChecked = todoItem.isComplete
            todoItemTitle.text = todoItem.title

            checkBox.setOnCheckedChangeListener { _, isChecked ->
                itemCheckedChangeCallback(todoItem, isChecked)
            }

            todoItemTitle.setOnClickListener {
                itemClickCallback(todoItem)
            }
        }
    }
}