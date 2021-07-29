package com.backend.koffiechefs.config

import com.google.cloud.firestore.Firestore
import com.google.firebase.cloud.FirestoreClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class FirebaseConfig {
    @Bean
    fun getDb(): Firestore {
        return FirestoreClient.getFirestore();
    }
}
