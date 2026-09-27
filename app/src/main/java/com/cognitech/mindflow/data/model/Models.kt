package com.cognitech.mindflow.data.model

/** Espejo local de la entidad User del backend (iam). */
data class User(
    val id: Long,
    val email: String,
    val name: String,
    val createdAt: Long,
)

/** Espejo local de la entidad JournalEntry del backend (journal). */
data class JournalEntry(
    val id: Long,
    val userId: Long,
    val content: String,
    val category: String,
    val sentiment: String,
    val aiResponse: String?,
    val createdAt: Long,
)

val JournalCategories = listOf("Estudios", "Trabajo", "Personal", "Salud", "Relaciones")
