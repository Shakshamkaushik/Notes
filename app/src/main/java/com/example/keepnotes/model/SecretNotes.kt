package com.example.keepnotes.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "secret_notes")
data class SecretNotes(
    val title: String,
    val des: String,
    val date: String,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)
