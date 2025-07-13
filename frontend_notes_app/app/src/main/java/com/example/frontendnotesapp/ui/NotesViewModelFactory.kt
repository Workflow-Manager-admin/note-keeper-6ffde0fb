package com.example.frontendnotesapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.frontendnotesapp.data.NotesRepository
import com.example.frontendnotesapp.data.SupabaseConfig
import android.content.Context

class NotesViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val (url, key) = SupabaseConfig.load(context)
        val repo = NotesRepository(url, key)
        return NotesViewModel(repo) as T
    }
}
