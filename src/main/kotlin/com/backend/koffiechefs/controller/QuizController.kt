package com.backend.koffiechefs.controller

import com.backend.koffiechefs.model.QuizQuestion
import com.backend.koffiechefs.service.QuizService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
class QuizController(private val quizService: QuizService) {
    @GetMapping("/quiz-questions/{id}")
    fun getQuestions(@PathVariable id: String): QuizQuestion {
        try {
            return quizService.getQuestion(id)
        } catch (exception: Exception) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find Quiz Question", exception)
        }
    }

    @GetMapping("/quiz-questions")
    fun getQuestions(): List<QuizQuestion> {
        try {
            return quizService.getQuestions()
        } catch (exception: Exception) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find Quiz Questions", exception)
        }
    }

    @PostMapping("/quiz-questions")
    fun addQuestion(@RequestBody quizQuestion: QuizQuestion) {
        return quizService.addQuestion(quizQuestion)
    }

    @PutMapping("/quiz-questions/{id}")
    fun updateQuestion(@PathVariable id: String, @RequestBody quizQuestion: QuizQuestion) {
        return quizService.updateQuestion(id, quizQuestion)
    }

    @DeleteMapping("/quiz-questions/{id}")
    fun deleteQuestion(@PathVariable id: String) {
        return quizService.deleteQuestion(id)
    }
}
