package com.zachtib.simpletodo.ui.todolistadapter

import androidx.recyclerview.widget.DiffUtil
import com.zachtib.simpletodo.models.TodoItem


// A class used by our ListAdapter in order to determine if two items are the same as
// the underlying data changes.
class TodoItemDiffCallback : DiffUtil.ItemCallback<TodoItem>() {

    // Determines if two objects represent the same item, regardless of their contents.
    // To do so, we just compare their ids, if they are the same, they represent the same
    // item.
    override fun areItemsTheSame(oldItem: TodoItem, newItem: TodoItem): Boolean {
        return oldItem.id == newItem.id
    }

    // Determines if the contents of two items are exactly the same. Because TodoItem is a
    // data class, we can just use the == operator here.
    override fun areContentsTheSame(oldItem: TodoItem, newItem: TodoItem): Boolean {
        return oldItem == newItem
    }
}