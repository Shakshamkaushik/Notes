package com.example.keepnotes.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.keepnotes.database.NotesDataBase
import com.example.keepnotes.mode.Notes
import com.example.keepnotes.model.SecretNotes
import com.example.keepnotes.model.Todo
import com.example.keepnotes.repository.NotesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NotesViewModel(application: Application) : AndroidViewModel(application) {

    val repo: NotesRepository
    val allNotes: LiveData<List<Notes>>
    val allSecretNotes: LiveData<List<SecretNotes>>
    val allTodoNotes: LiveData<List<Todo>>

    init {
        val dao = NotesDataBase.getDatabase(application).getNotes()
        repo = NotesRepository(dao)
        allNotes = repo.getAllNotes()
        allSecretNotes = repo.getAllASecretNotes()
        allTodoNotes = repo.getAllTodoTask()
    }

    fun addNote(note: Notes) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.insertNote(note)
        }
    }

    fun delNote(note: Notes) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.deleteNote(note)
        }
    }

    fun updateNote(note: Notes) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.updateNote(note)
        }
    }

    fun addSecretNote(secretNotes: SecretNotes) {
        viewModelScope.launch {
            repo.insertSecretNote(secretNotes)
        }
    }

    fun updateSecretNote(secretNotes: SecretNotes) {
        viewModelScope.launch {
            repo.updateSecretNote(secretNotes)
        }
    }

    fun deleteSecretNote(secretNotes: SecretNotes) {
        viewModelScope.launch {
            repo.deleteSecretNote(secretNotes)
        }
    }

    fun addTodoNote(todo: Todo) {
        viewModelScope.launch {
            repo.insertTodoTask(todo)
        }
    }

    fun deleteTodoNote(todo: Todo) {
        viewModelScope.launch {
            repo.deleteTodoTask(todo)
        }
    }

    fun updateTodoNote(todo: Todo) {
        viewModelScope.launch {
            repo.updateTodoTask(todo)
        }
    }

}