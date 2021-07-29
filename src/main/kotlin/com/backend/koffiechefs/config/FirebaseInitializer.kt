package com.backend.koffiechefs.config

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import org.springframework.stereotype.Service
import java.io.FileInputStream
import javax.annotation.PostConstruct


@Service
class FirebaseInitializer {
    @PostConstruct
    fun initialize() {
        val serviceAccount = FirebaseInitializer::class.java.getResourceAsStream("/serviceAccount.json")

        val options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl("https://koffiechefs.firebaseio.com")
                .build()

        FirebaseApp.initializeApp(options)
    }
}
