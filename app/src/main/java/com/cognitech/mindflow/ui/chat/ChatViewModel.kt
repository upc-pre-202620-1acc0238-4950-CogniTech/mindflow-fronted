package com.cognitech.mindflow.ui.chat

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cognitech.mindflow.application.ChatUseCases
import com.cognitech.mindflow.domain.model.ChatMessage
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class ChatState(
    val open: Boolean = false,
    val draft: String = "",
    val typing: Boolean = false,
    val messages: List<ChatMessage> = emptyList(),
)

/**
 * Chat flotante con MindFlow AI. Vive a nivel de Activity para conservar la conversación
 * al navegar entre pantallas.
 */
class ChatViewModel(private val chat: ChatUseCases) : ViewModel() {

    var state by mutableStateOf(ChatState(messages = listOf(chat.welcome())))
        private set

    fun toggle() { state = state.copy(open = !state.open) }
    fun close() { state = state.copy(open = false) }
    fun onDraftChange(value: String) { state = state.copy(draft = value) }

    fun send() {
        val text = state.draft.trim()
        if (text.isEmpty() || state.typing) return
        state = state.copy(
            draft = "",
            typing = true,
            messages = state.messages + ChatMessage(text, fromUser = true),
        )
        viewModelScope.launch {
            delay(TYPING_DELAY_MS)
            val reply = chat.reply(text)
            state = state.copy(typing = false, messages = state.messages + reply)
        }
    }

    private companion object {
        const val TYPING_DELAY_MS = 900L
    }
}
