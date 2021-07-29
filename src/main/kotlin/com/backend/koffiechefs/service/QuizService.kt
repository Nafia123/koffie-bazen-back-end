package com.backend.koffiechefs.service

import com.backend.koffiechefs.config.FirebaseConfig
import com.backend.koffiechefs.model.QuizAnswer
import com.backend.koffiechefs.model.QuizQuestion
import com.backend.koffiechefs.utils.getField
import org.springframework.stereotype.Service

@Service
class QuizService(private val firebaseConfig: FirebaseConfig, private val coffeeService: CoffeeService) {
    fun getQuestion(id: String): QuizQuestion {
        val document = firebaseConfig.getDb().collection("quiz").document(id)
        return QuizQuestion(
            id,
            document.get().get().get("image") as String,
            document.get().get().get("question") as String,
            document.get().get().get("relevantField") as String,
            generateAnswers(document.get().get().get("relevantField") as String)
        )
    }

    fun getQuestions(): List<QuizQuestion> {
        val quizDocumentPath = "quiz"
        // Get collection snapshot of questions
        val questionCollection = firebaseConfig.getDb()
            .collection(quizDocumentPath)

        // Map snapshot into Question List
        return questionCollection
            .get()
            .get()
            .documents.map { question ->
                QuizQuestion(
                    question.id,
                    question.data["image"] as String,
                    question.data["question"] as String,
                    question.data["relevantField"] as String,
                    generateAnswers(question.data["relevantField"] as String)
                )
            }
    }

    private fun generateAnswers(relevantField: String): List<QuizAnswer> {
        val coffees = coffeeService.getCoffees()
        val answers = arrayListOf<QuizAnswer>()

        coffees.forEach {
            answers.add(QuizAnswer(it.id, it.getField<String>(relevantField)))
        }

        return answers
    }

    fun addQuestion(quizQuestion: QuizQuestion) {
        val document = firebaseConfig.getDb().collection("quiz").document()
        val fields = hashMapOf(
            "image" to quizQuestion.image,
            "question" to quizQuestion.question,
            "relevantField" to quizQuestion.relevantField
        )

        document.set(fields as Map<String, Any>)
    }

    fun updateQuestion(id: String, quizQuestion: QuizQuestion) {
        val document = firebaseConfig.getDb().collection("quiz").document(id)
        val fields = hashMapOf(
            "image" to quizQuestion.image,
            "question" to quizQuestion.question,
            "relevantField" to quizQuestion.relevantField
        )

        document.update(fields as Map<String, Any>)
    }

    fun deleteQuestion(id: String) {
        firebaseConfig.getDb().collection("quiz").document(id)
            .delete()
    }
}
