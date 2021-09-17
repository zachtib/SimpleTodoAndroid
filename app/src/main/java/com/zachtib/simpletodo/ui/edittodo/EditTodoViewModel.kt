package com.zachtib.simpletodo.ui.edittodo

import androidx.lifecycle.ViewModel

class EditTodoViewModel(
    // If set, we are editing an existing item, otherwise we are creating a new one
    val editingTodoItemId: Int
) : ViewModel() {
}