package com.backend.koffiechefs.service

import com.backend.koffiechefs.config.FirebaseConfig
import com.backend.koffiechefs.model.Roaster
import com.backend.koffiechefs.model.User
import com.google.cloud.firestore.FieldPath
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserRecord
import org.springframework.stereotype.Service
import java.util.*

@Service
class RoasterService(private val firebaseConfig: FirebaseConfig) {

    fun createApplication(roaster: Roaster): Int{
        try {
            val roasterData = hashMapOf(
                    "firstName" to roaster.firstName,
                    "lastName" to roaster.lastName,
                    "email" to roaster.email,
                    "password" to roaster.password,
                    "address" to roaster.address,
                    "zipcode" to roaster.zipcode,
                    "phoneNumber" to roaster.phoneNumber,
                    "companyName" to roaster.companyName,
                    "reviewed" to false,
                    "dateSent" to roaster.dateSent
            )

            firebaseConfig.getDb().collection("roasterApplication").document().set(roasterData as Map<String, Any>)
            return 1
        }catch (error: FirebaseException){
            println(error)
            return 0
        }
    }

    fun declineApplication(roaster: Roaster): Int{
        val collection = firebaseConfig.getDb().collection("roasterApplication")
        return try {
            val documentId = collection.whereEqualTo("email", roaster.email).get().get().documents[0].id
            hashMapOf("reviewed" to true)
            collection.document(documentId).update(hashMapOf("reviewed" to true) as Map<String,Any>)
            1
        }catch (e : FirebaseException){
            println("Application Doesn't exist " + e.message);
            0
        }
    }

    fun getAllApplications(): List<Roaster>{
        return firebaseConfig.getDb().collection("roasterApplication").whereEqualTo("reviewed",false)
                .get().get().documents.map { document ->
                    Roaster(document.id,document.data["firstName"] as String, document.data["lastName"] as String, document.data["email"] as String,
                    document.data["password"] as String, document.data["phoneNumber"] as String, document.data["address"] as String, document.data["zipcode"] as String,
                    document.data["companyName"] as String, document.data["dateSent"] as String)
                }
    }



}