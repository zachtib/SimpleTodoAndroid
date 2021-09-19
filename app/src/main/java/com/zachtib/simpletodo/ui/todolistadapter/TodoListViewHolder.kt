package com.zachtib.simpletodo.ui.todolistadapter

import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.zachtib.simpletodo.R
import com.zachtib.simpletodo.models.TodoItem


// Holds a single instance of our repeating list item, and is capable of being bound
// and re-bound to different TodoItem instances.
class TodoListViewHolder(
    itemView: View,
    private val itemClickCallback: (TodoItem) -> Unit,
    private val itemCheckedChangeCallback: (TodoItem, Boolean) -> Unit,
) : RecyclerView.ViewHolder(itemView) {

    // The views inside of our itemView that we care about
    private val checkBox = itemView.findViewById<CheckBox>(R.id.checkBox)
    private val todoItemTitle = itemView.findViewById<TextView>(R.id.todoItemTitle)

    // Fills this view with the contents of todoItem
    fun bindTodoItem(todoItem: TodoItem) {
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