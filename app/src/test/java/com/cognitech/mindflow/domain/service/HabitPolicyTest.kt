package com.cognitech.mindflow.domain.service

import com.cognitech.mindflow.domain.model.HabitCategory
import com.cognitech.mindflow.domain.model.JournalEntry
import com.cognitech.mindflow.domain.model.Sentiment
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate

class HabitPolicyTest {
    @Test fun `classifies habit from its name`() {
        assertEquals(HabitCategory.STUDY, HabitPolicy.categoryFor("Estudiar estadística"))
    }

    @Test fun `counts consecutive days ending yesterday when today is pending`() {
        val today = LocalDate.of(2026, 9, 28)
        assertEquals(3, HabitPolicy.streak(setOf(today.minusDays(1), today.minusDays(2), today.minusDays(3)), today))
    }

    @Test fun `detects stress after two negative recent entries`() {
        fun entry(sentiment: Sentiment) = JournalEntry(1, 1, "", "", "", sentiment, null, 0)
        assertTrue(HabitPolicy.stressDetected(listOf(entry(Sentiment.NEGATIVE), entry(Sentiment.NEGATIVE), entry(Sentiment.POSITIVE))))
    }
}
