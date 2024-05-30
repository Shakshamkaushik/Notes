package com.example.keepnotes.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "todo_task_table")
data class Todo (
    val todoTask : String,
    @PrimaryKey(autoGenerate = true)
    val id: Int= 0,

)