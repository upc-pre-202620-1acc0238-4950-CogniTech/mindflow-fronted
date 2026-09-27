package com.cognitech.mindflow.ui.journal

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cognitech.mindflow.data.model.JournalEntry
import com.cognitech.mindflow.data.repository.AuthRepository
import com.cognitech.mindflow.data.repository.JournalRepository
import com.cognitech.mindflow.ui.common.toLocalDate
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth

data class JournalState(
    val entries: List<JournalEntry> = emptyList(),
    val query: String = "",
    val category: String = "Todos",
    val sentiment: String? = null,
    val date: LocalDate? = null,
    val month: YearMonth = YearMonth.now(),
) {
    val filtered: List<JournalEntry>
        get() = entries.filter { e ->
            (query.isBlank() || e.title.contains(query, true) || e.content.contains(query, true)) &&
                (category == "Todos" || e.category == category) &&
                (sentiment == null || e.sentiment == sentiment) &&
                (date == null || e.createdAt.toLocalDate() == date)
        }
}

class JournalViewModel(
    private val authRepository: AuthRepository,
    private val journalRepository: JournalRepository,
) : ViewModel() {

    var state by mutableStateOf(JournalState())
        private set

    fun load() {
        viewModelScope.launch {
            val user = authRepository.currentUser() ?: return@launch
            state = state.copy(entries = journalRepository.listByUser(user.id))
        }
    }

    fun onQueryChange(value: String) { state = state.copy(query = value) }
    fun onCategoryChange(value: String) { state = state.copy(category = value) }
    fun onSentimentChange(value: String?) { state = state.copy(sentiment = value) }
    fun onDateChange(value: LocalDate?) { state = state.copy(date = value) }
    fun onDayClick(day: LocalDate) { state = state.copy(date = if (state.date == day) null else day) }

    fun toggleMonth() {
        val current = YearMonth.now()
        state = state.copy(month = if (state.month == current) current.minusMonths(1) else current)
    }

    fun logout() = authRepository.logout()
}
