package com.zachtib.simpletodo.ui.todolistadapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.zachtib.simpletodo.R
import com.zachtib.simpletodo.models.TodoItem


// Adapts the contents of a list of TodoItems into a RecyclerView. Extends the abstract class
// ListAdapter, which is included in the RecyclerView library to do most of the heavy lifting
// for us. We just need to help it by providing functionality to create new view holders, to
// bind data to those view holders, and to compare items in the list (done via TodoItemDiffCallback)
// in order to determine when items change.
class TodoListAdapter : ListAdapter<TodoItem, TodoListViewHolder>(TodoItemDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoListViewHolder {

        // Get an instance of LayoutInflater from our parent, so we can inflate layout files
        val layoutInflator = LayoutInflater.from(parent.context)

        // Inflate our list item inside of parent, but don't attach (yet), RecyclerView will handle
        // that for us.
        val itemView = layoutInflator.inflate(R.layout.item_todoitem, parent, false)

        // Wrap the inflated view in our ViewHolder class and return it.
        return TodoListViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TodoListViewHolder, position: Int) {

        // Get the instance of TodoItem that is at the given position in our list.
        val todoItem = getItem(position)

        // Then bind it to our view holder
        holder.bindTodoItem(todoItem)
    }
}