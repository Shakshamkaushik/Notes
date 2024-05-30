package com.example.keepnotes.mode

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "free_notes")
data class Notes(
    val title: String,
    val des: String,
    val date: String,
    @PrimaryKey(autoGenerate = true)
    val id:Int = 0)
