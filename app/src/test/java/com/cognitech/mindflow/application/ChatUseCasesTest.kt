package com.cognitech.mindflow.application

import com.cognitech.mindflow.domain.model.Sentiment
import com.cognitech.mindflow.domain.port.AiResponder
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test

class ChatUseCasesTest {
    private val ai = object : AiResponder {
        override fun detectSentiment(text: String) = Sentiment.NEGATIVE
        override fun title(text: String) = ""
        override fun respond(text: String, sentiment: Sentiment) = "${sentiment.name}: $text"
        override fun weeklyInsight(positive: Int, neutral: Int, negative: Int, topCategory: String?) = ""
    }
    private val chat = ChatUseCases(ai)

    @Test fun `replies as the assistant using the detected sentiment`() {
        val reply = runBlocking { chat.reply("Estoy cansado") }
        assertEquals("NEGATIVE: Estoy cansado", reply.text)
        assertFalse(reply.fromUser)
    }

    @Test fun `welcome message comes from the assistant`() {
        assertFalse(chat.welcome().fromUser)
    }
}
