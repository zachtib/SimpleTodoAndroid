package com.zachtib.simpletodo.models

import androidx.room.Entity
import androidx.room.PrimaryKey

// Tells the Room compiler that this is a database entity
@Entity
data class TodoItem(
    // Indicates our primary key, and that Room should generate ids for us
    @PrimaryKey(autoGenerate = true) val id: Int,
    val title: String,
    val isComplete: Boolean,
    val description: String,
)