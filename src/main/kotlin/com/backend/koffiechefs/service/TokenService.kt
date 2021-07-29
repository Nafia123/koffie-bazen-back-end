package com.backend.koffiechefs.service

import com.backend.koffiechefs.model.User
import com.google.firebase.auth.FirebaseAuth
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class TokenService(private val userService: UserService) {
    fun isTokenValid(token: String?): Boolean {
        return try {
            if (token != null) {
                val cleanedToken = token.replace("Bearer ", "").trim()
                val decodedToken = FirebaseAuth.getInstance().verifyIdToken(cleanedToken)

                    decodedToken.uid != null
            } else {
                false
            }
        } catch (exception: Exception) {
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token Invalid", exception)
        }
    }

    fun parseToken(token: String?): User {
        try {
            val decodedToken = FirebaseAuth.getInstance().verifyIdToken(token)
            val uid = decodedToken.uid

            val filteredUsers = userService.getAllUsers().filter {
                it.id == uid
            }

            return filteredUsers[0]
        } catch (exception: Exception) {
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token Invalid", exception)
        }
    }
}
