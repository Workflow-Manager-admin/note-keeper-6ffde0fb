package com.example.frontendnotesapp.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// PUBLIC_INTERFACE
@Serializable
data class Note(
    @SerialName("id")
    val id: String,
    @SerialName("title")
    val title: String,
    @SerialName("content")
    val content: String
)
