package com.example.frontendnotesapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontendnotesapp.data.Note
import com.example.frontendnotesapp.data.NotesRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class NotesViewModel(
    private val repository: NotesRepository? = null
) : ViewModel() {
    private val _notes = MutableStateFlow<List<Note>>(emptyList())
    val filteredNotes: StateFlow<List<Note>> = _notes

    private var allNotes: List<Note> = emptyList()
    private var lastSearch: String = ""

    init {
        repository?.let {
            viewModelScope.launch { loadNotes() }
        }
    }

    // PUBLIC_INTERFACE
    suspend fun loadNotes() {
        repository?.let {
            allNotes = it.getNotes()
            _notes.value = allNotes
            lastSearch = ""
        }
    }

    // PUBLIC_INTERFACE
    fun searchNotes(query: String) {
        if (repository == null) return
        if (query.isBlank()) {
            _notes.value = allNotes
            lastSearch = ""
        } else {
            viewModelScope.launch {
                val result = repository.searchNotes(query)
                _notes.value = result
                lastSearch = query
            }
        }
    }

    // PUBLIC_INTERFACE
    fun addNote(title: String, content: String) {
        if (repository == null) return
        viewModelScope.launch {
            val created = repository.addNote(title, content)
            if (created != null) {
                allNotes = listOf(created) + allNotes
                _notes.value = if (lastSearch.isBlank()) allNotes else repository.searchNotes(lastSearch)
            }
        }
    }

    // PUBLIC_INTERFACE
    fun updateNote(id: String, title: String, content: String) {
        if (repository == null) return
        viewModelScope.launch {
            val success = repository.updateNote(id, title, content)
            if (success) {
                allNotes = allNotes.map {
                    if (it.id == id) it.copy(title = title, content = content) else it
                }
                _notes.value = if (lastSearch.isBlank()) allNotes else repository.searchNotes(lastSearch)
            }
        }
    }

    // PUBLIC_INTERFACE
    fun deleteNote(id: String) {
        if (repository == null) return
        viewModelScope.launch {
            val success = repository.deleteNote(id)
            if (success) {
                allNotes = allNotes.filter { it.id != id }
                _notes.value = if (lastSearch.isBlank()) allNotes else repository.searchNotes(lastSearch)
            }
        }
    }

    // PUBLIC_INTERFACE
    fun getNoteById(id: String): Note? = allNotes.find { it.id == id }
}
