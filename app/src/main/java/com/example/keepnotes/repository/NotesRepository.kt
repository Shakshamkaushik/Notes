package com.example.keepnotes.repository

import androidx.lifecycle.LiveData
import com.example.keepnotes.database.NotesDao
import com.example.keepnotes.mode.Notes
import com.example.keepnotes.model.SecretNotes
import com.example.keepnotes.model.Todo

class NotesRepository(val dao: NotesDao) {

    fun getAllNotes(): LiveData<List<Notes>> {
        return dao.getAllNotes()
    }

    suspend fun insertNote(notes: Notes) {
        dao.insertNote(notes)
    }

    suspend fun deleteNote(notes: Notes) {
        dao.deleteNote(notes)
    }

    suspend fun updateNote(notes: Notes) {
        dao.updateNote(notes)
    }


    fun getAllASecretNotes(): LiveData<List<SecretNotes>> {
        return dao.getAllSecretNotes()
    }

    suspend fun insertSecretNote(secretNotes: SecretNotes) {
        dao.insertSecretNote(secretNotes)
    }

    suspend fun deleteSecretNote(secretNotes: SecretNotes) {
        dao.deleteSecretNote(secretNotes)
    }

    suspend fun updateSecretNote(secretNotes: SecretNotes) {
        dao.updateSecretNote(secretNotes)
    }


    suspend fun updateTodoTask(todo: Todo) {
        dao.updateTodoTask(todo)
    }

    suspend fun insertTodoTask(todo: Todo) {
        dao.insertTodoTask(todo)
    }

    suspend fun deleteTodoTask(todo: Todo) {
        dao.deleteTodoTask(todo)
    }

    fun getAllTodoTask(): LiveData<List<Todo>> {
       return dao.getAllTodoTAsk()
    }
}