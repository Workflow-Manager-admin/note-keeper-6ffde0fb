package com.example.frontendnotesapp.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import io.ktor.http.*

// PUBLIC_INTERFACE
class NotesRepository(
    private val supabaseUrl: String,
    private val supabaseKey: String
) {
    private val httpClient = HttpClient(CIO) {
        install(ContentNegotiation) {
            // The 'json' extension is resolved by the proper import above.
            json(Json { ignoreUnknownKeys = true })
        }
    }

    private fun notesTableUrl() = "${supabaseUrl}/rest/v1/notes"

    private fun baseHeaders(): Map<String, String> = mapOf(
        "apikey" to supabaseKey,
        "Authorization" to "Bearer $supabaseKey"
    )

    // PUBLIC_INTERFACE
    suspend fun getNotes(): List<Note> = withContext(Dispatchers.IO) {
        val response: HttpResponse = httpClient.get(notesTableUrl()) {
            headers {
                for ((k, v) in baseHeaders()) append(k, v)
                append("Accept-Profile", "public")
            }
            parameter("select", "*")
            parameter("order", "id.desc")
        }
        val bodyStr = response.bodyAsText()
        Json.decodeFromString<List<Note>>(bodyStr)
    }

    // PUBLIC_INTERFACE
    suspend fun addNote(title: String, content: String): Note? = withContext(Dispatchers.IO) {
        val response: HttpResponse = httpClient.post(notesTableUrl()) {
            headers {
                for ((k, v) in baseHeaders()) append(k, v)
                append("Prefer", "return=representation")
            }
            contentType(ContentType.Application.Json)
            setBody(buildJsonObject {
                put("title", title)
                put("content", content)
            }.toString())
        }
        val bodyStr = response.bodyAsText()
        val notes = Json.decodeFromString<List<Note>>(bodyStr)
        notes.firstOrNull()
    }

    // PUBLIC_INTERFACE
    suspend fun updateNote(id: String, title: String, content: String): Boolean = withContext(Dispatchers.IO) {
        val url = "${notesTableUrl()}?id=eq.$id"
        val response: HttpResponse = httpClient.patch(url) {
            headers {
                for ((k, v) in baseHeaders()) append(k, v)
                append("Prefer", "return=representation")
            }
            contentType(ContentType.Application.Json)
            setBody(buildJsonObject {
                put("title", title)
                put("content", content)
            }.toString())
        }
        val bodyStr = response.bodyAsText()
        bodyStr.contains(id)
    }

    // PUBLIC_INTERFACE
    suspend fun deleteNote(id: String): Boolean = withContext(Dispatchers.IO) {
        val url = "${notesTableUrl()}?id=eq.$id"
        val response: HttpResponse = httpClient.delete(url) {
            headers {
                for ((k, v) in baseHeaders()) append(k, v)
            }
        }
        // supabase returns [] (empty array) on successful delete
        response.bodyAsText() == "[]"
    }

    // PUBLIC_INTERFACE
    suspend fun searchNotes(search: String): List<Note> = withContext(Dispatchers.IO) {
        val url = notesTableUrl()
        val response: HttpResponse = httpClient.get(url) {
            headers {
                for ((k, v) in baseHeaders()) append(k, v)
                append("Accept-Profile", "public")
            }
            parameter("select", "*")
            parameter("or", "(title.ilike.*$search*,content.ilike.*$search*)")
            parameter("order", "id.desc")
        }
        val bodyStr = response.bodyAsText()
        Json.decodeFromString<List<Note>>(bodyStr)
    }
}
