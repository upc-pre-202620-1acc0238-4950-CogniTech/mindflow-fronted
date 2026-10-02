package com.cognitech.mindflow.ui.chat

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cognitech.mindflow.data.ai.LocalAiResponder
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class ChatMessage(val id: Long, val text: String, val fromUser: Boolean)

data class ChatState(
    val open: Boolean = false,
    val draft: String = "",
    val typing: Boolean = false,
    val messages: List<ChatMessage> = listOf(ChatMessage(0, WELCOME_MESSAGE, fromUser = false)),
)

/**
 * Chat flotante con MindFlow AI. Vive a nivel de Activity para conservar la conversación
 * al navegar entre pantallas. Hoy responde con LocalAiResponder; se reemplazará por
 * `POST /chat/conversations` del backend.
 */
class ChatViewModel(private val aiResponder: LocalAiResponder) : ViewModel() {

    var state by mutableStateOf(ChatState())
        private set

    private var nextId = 1L

    fun toggle() { state = state.copy(open = !state.open) }
    fun close() { state = state.copy(open = false) }
    fun onDraftChange(value: String) { state = state.copy(draft = value) }

    fun send() {
        val text = state.draft.trim()
        if (text.isEmpty() || state.typing) return
        state = state.copy(
            draft = "",
            typing = true,
            messages = state.messages + ChatMessage(nextId++, text, fromUser = true),
        )
        viewModelScope.launch {
            delay(TYPING_DELAY_MS)
            val reply = aiResponder.respond(text, aiResponder.detectSentiment(text))
            state = state.copy(
                typing = false,
                messages = state.messages + ChatMessage(nextId++, reply, fromUser = false),
            )
        }
    }

    private companion object {
        const val TYPING_DELAY_MS = 900L
    }
}

private const val WELCOME_MESSAGE =
    "Hola, soy MindFlow AI. Estoy aquí para escucharte. ¿Cómo te sientes en este momento?"
