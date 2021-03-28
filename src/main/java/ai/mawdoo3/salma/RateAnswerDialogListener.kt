package ai.mawdoo3.salma

import java.io.Serializable

interface RateAnswerDialogListener : Serializable {
    fun rateAnswer(answerId: String, isGood: Boolean)
}