package com.example.frontendnotesapp.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.compose.foundation.clickable

import com.example.frontendnotesapp.data.Note

// PUBLIC_INTERFACE
@Composable
fun NotesApp(viewModel: NotesViewModel) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "main") {
        composable("main") {
            MainScreen(
                viewModel = viewModel,
                onAddNote = { navController.navigate("edit") },
                onEditNote = { navController.navigate("edit/${it.id}") }
            )
        }
        composable(
            "edit/{noteId?}",
            arguments = listOf(navArgument("noteId") { type = NavType.StringType; nullable = true })
        ) { backStackEntry ->
            val noteId = backStackEntry.arguments?.getString("noteId")
            EditNoteScreen(
                viewModel = viewModel,
                noteId = noteId,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}

// PUBLIC_INTERFACE
@Composable
fun MainScreen(
    viewModel: NotesViewModel,
    onAddNote: () -> Unit,
    onEditNote: (Note) -> Unit
) {
    val notes by viewModel.filteredNotes.collectAsState()
    var searchText by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Notes") },
                backgroundColor = MaterialTheme.colors.primary
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { onAddNote() }) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    ) { padding ->
        Column(Modifier
            .padding(padding)
            .fillMaxSize()) {
            OutlinedTextField(
                value = searchText,
                onValueChange = {
                    searchText = it
                    viewModel.searchNotes(it)
                },
                placeholder = { Text("Search notes...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                singleLine = true,
                trailingIcon = {
                    Icon(Icons.Default.Search, contentDescription = "Search")
                }
            )
            Divider()
            if (notes.isEmpty()) {
                Box(
                    Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) { Text("No notes found.") }
            } else {
                LazyColumn {
                    items(notes) { note ->
                        NoteListItem(
                            note = note,
                            onEdit = { onEditNote(note) },
                            onDelete = { viewModel.deleteNote(note.id) }
                        )
                    }
                }
            }
        }
    }
}

// PUBLIC_INTERFACE
@Composable
fun NoteListItem(note: Note, onEdit: () -> Unit, onDelete: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp),
        elevation = 2.dp
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                Modifier
                    .weight(1f)
                    .clickable { onEdit() }
            ) {
                Text(note.title, style = MaterialTheme.typography.h6)
                Spacer(Modifier.height(4.dp))
                Text(
                    if (note.content.length > 60)
                        note.content.substring(0, 60) + "..."
                    else
                        note.content,
                    style = MaterialTheme.typography.body2,
                    maxLines = 2
                )
            }
            IconButton(onClick = { onEdit() }) {
                Icon(Icons.Default.Edit, contentDescription = "Edit")
            }
            IconButton(onClick = { onDelete() }) {
                Icon(Icons.Default.Delete, contentDescription = "Delete")
            }
        }
    }
}

// PUBLIC_INTERFACE
@Composable
fun EditNoteScreen(
    viewModel: NotesViewModel,
    noteId: String?,
    onNavigateBack: () -> Unit
) {
    val editingNote = if (noteId != null) viewModel.getNoteById(noteId) else null
    var title by remember { mutableStateOf(editingNote?.title ?: "") }
    var content by remember { mutableStateOf(editingNote?.content ?: "") }
    var showError by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (noteId != null) "Edit Note" else "New Note") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = {
                    title = it
                    if (showError) showError = false
                },
                label = { Text("Title") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(12.dp))
            OutlinedTextField(
                value = content,
                onValueChange = {
                    content = it
                    if (showError) showError = false
                },
                label = { Text("Content") },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 120.dp, max = 300.dp),
                maxLines = 10
            )
            if (showError) {
                Text("Title and content cannot be empty.", color = MaterialTheme.colors.error)
            }
            Spacer(Modifier.height(16.dp))
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = {
                        if (title.isBlank() || content.isBlank()) {
                            showError = true
                        } else {
                            if (noteId == null) {
                                viewModel.addNote(title, content)
                            } else {
                                viewModel.updateNote(noteId, title, content)
                            }
                            onNavigateBack()
                        }
                    }
                ) {
                    Text("Save")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    val fakeNotes = listOf(
        Note("1", "Sample 1", "Body text for the first note goes here."),
        Note("2", "Sample 2", "Another note goes here which is much longer and possibly truncated in the list view.")
    )

    // Local stub for exactly what MainScreen uses, using the same filteredNotes API
    val previewState = kotlinx.coroutines.flow.MutableStateFlow(fakeNotes)
    val fakeViewModel = object {
        val filteredNotes: kotlinx.coroutines.flow.StateFlow<List<Note>> = previewState
        fun searchNotes(query: String) {}
        fun deleteNote(id: String) {}
    }

    // Use unsafe cast ONLY in preview to bypass type system
    @Suppress("UNCHECKED_CAST")
    MaterialTheme {
        Surface {
            MainScreen(
                viewModel = fakeViewModel as NotesViewModel, // Only the collected filteredNotes, searchNotes, and deleteNote are used in preview
                onAddNote = {},
                onEditNote = {}
            )
        }
    }
}
