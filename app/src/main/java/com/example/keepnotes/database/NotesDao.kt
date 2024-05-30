package com.example.keepnotes.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.keepnotes.mode.Notes
import com.example.keepnotes.model.SecretNotes
import com.example.keepnotes.model.Todo

@Dao
interface NotesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: Notes)

    @Delete
    suspend fun deleteNote(note: Notes)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateNote(note: Notes)

    @Query("SELECT * FROM free_notes")
    fun getAllNotes(): LiveData<List<Notes>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSecretNote(secretNotes: SecretNotes)

    @Delete
    suspend fun deleteSecretNote(secretNotes: SecretNotes)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateSecretNote(secretNotes: SecretNotes)

    @Query("SELECT * FROM secret_notes")
    fun getAllSecretNotes(): LiveData<List<SecretNotes>>

    @Query("SELECT * FROM todo_task_table")
    fun getAllTodoTAsk(): LiveData<List<Todo>>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateTodoTask(todo: Todo)

    @Delete
    suspend fun deleteTodoTask(todo: Todo)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTodoTask(todo: Todo)
}