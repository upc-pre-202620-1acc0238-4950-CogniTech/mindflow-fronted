package com.cognitech.mindflow.ui.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cognitech.mindflow.data.model.JournalCategories
import com.cognitech.mindflow.data.model.JournalEntry
import com.cognitech.mindflow.data.model.User
import com.cognitech.mindflow.data.repository.AuthRepository
import com.cognitech.mindflow.data.repository.JournalRepository
import kotlinx.coroutines.launch

data class HomeState(
    val user: User? = null,
    val draft: String = "",
    val category: String = JournalCategories.first(),
    val entries: List<JournalEntry> = emptyList(),
    val lastAiResponse: String? = null,
    val saving: Boolean = false,
    val showAllHistory: Boolean = false,
)

class HomeViewModel(
    private val authRepository: AuthRepository,
    private val journalRepository: JournalRepository,
) : ViewModel() {

    var state by mutableStateOf(HomeState())
        private set

    init {
        viewModelScope.launch {
            val user = authRepository.currentUser() ?: return@launch
            val entries = journalRepository.listByUser(user.id)
            state = state.copy(user = user, entries = entries, lastAiResponse = entries.firstOrNull()?.aiResponse)
        }
    }

    fun onDraftChange(value: String) { state = state.copy(draft = value) }
    fun onCategoryChange(value: String) { state = state.copy(category = value) }
    fun toggleHistory() { state = state.copy(showAllHistory = !state.showAllHistory) }

    fun save() {
        val user = state.user ?: return
        if (state.draft.isBlank() || state.saving) return
        state = state.copy(saving = true)
        viewModelScope.launch {
            val entry = journalRepository.create(user.id, state.draft, state.category)
            state = state.copy(
                draft = "",
                saving = false,
                entries = listOf(entry) + state.entries,
                lastAiResponse = entry.aiResponse,
            )
        }
    }

    fun logout() = authRepository.logout()
}
