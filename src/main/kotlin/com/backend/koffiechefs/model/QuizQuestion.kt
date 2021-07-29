package com.backend.koffiechefs.model

data class QuizQuestion(
    val id: String?,
    val image: String?,
    val question: String,
    val relevantField: String,
    val answers: List<QuizAnswer>?
)
