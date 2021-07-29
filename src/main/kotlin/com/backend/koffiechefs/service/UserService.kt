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
class UserService(private val firebaseConfig: FirebaseConfig) {

    fun updateUser(user: User){
//        val userId = firebaseConfig.getDb().collection("users")
//                .whereEqualTo("email", user.email).get().get()
//                .documents[0].id

        // Create Update Request for the user
        val updateRequest = UserRecord.UpdateRequest(user.id)
                .setEmail(user.email)
                .setEmailVerified(false)
        if (user.password.isNotEmpty()) updateRequest.setPassword(user.password)

        // Update the user
        FirebaseAuth.getInstance().updateUser(updateRequest)
        updateUserClaims(user.id, user.role)

        // Update the document of the update User
        val document = firebaseConfig.getDb().collection("users").document(user.id)
        val userData = hashMapOf(
                "firstName" to user.firstName,
                "lastName" to user.lastName,
                "email" to user.email,
                "role" to user.role,
                "phoneNumber" to user.phoneNumber)

        document.update(userData as Map<String, Any>)
    }

    fun getAllRoaster(): List<Roaster>{
        // Get Data out of Firestore based on the userIds
        return firebaseConfig.getDb().collection("users")
                .whereEqualTo("role", "1").get().get()
                .documents.map { document ->
                    val amount = document.data["amount"] as Long
                    Roaster(document.id, document.data["firstName"] as String, document.data["lastName"] as String,
                            document.data["email"] as String, "", document.data["phoneNumber"] as String, document.data["address"] as String,
                            document.data["zipcode"] as String, document.data["companyName"] as String, "",
                            amount.toInt())
                }
    }

    fun deleteUser(id: String){
        FirebaseAuth.getInstance().deleteUser(id)

        firebaseConfig.getDb().collection("users").document(id)
                .delete()
    }

    fun addUser(user: User): Int{
        try {
            // Create Request to create Firebase user
            val userRequest = UserRecord.CreateRequest()
                    .setEmail(user.email)
                    .setEmailVerified(false)
                    .setPassword(user.password)

            val userRecord = FirebaseAuth.getInstance().createUser(userRequest)
            // Get the newly generated User ID for the firestore
            val newUserId = userRecord.uid
            updateUserClaims(newUserId, user.role)
            // Create Object for User collection
            val userData = hashMapOf(
                    "firstName" to user.firstName,
                    "lastName" to user.lastName,
                    "email" to user.email,
                    "role" to user.role,
                    "phoneNumber" to user.phoneNumber
            )

            firebaseConfig.getDb().collection("users").document(newUserId)
                    .set(userData as Map<String, Any>)
            return 1
        }catch (error: FirebaseException){
            println(error)
            return 0
        }
    }

    fun addRoaster(roaster: Roaster): Int{
        try {
            // Create Request to create Firebase user
            val userRequest = UserRecord.CreateRequest()
                    .setEmail(roaster.email)
                    .setEmailVerified(false)
                    .setPassword(roaster.password)

            val userRecord = FirebaseAuth.getInstance().createUser(userRequest)
            // Get the newly generated User ID for the firestore
            val newUserId = userRecord.uid
            updateUserClaims(newUserId, "1")
            // Create Object for User collection
            val roasterData = hashMapOf(
                    "firstName" to roaster.firstName,
                    "lastName" to roaster.lastName,
                    "email" to roaster.email,
                    //Set amount sold to 0
                    "amount" to 0,
                    //Set role to roaster.
                    "role" to "1",
                    "address" to roaster.address,
                    "zipcode" to roaster.zipcode,
                    "phoneNumber" to roaster.phoneNumber,
                    "companyName" to roaster.companyName
            )

            firebaseConfig.getDb().collection("users").document(newUserId)
                    .set(roasterData as Map<String, Any>)


            val updateReview = hashMapOf("reviewed" to true)

            firebaseConfig.getDb().collection("roasterApplication").document(roaster.id).update(updateReview as Map<String, Any>)

            return 1
        }catch (error: FirebaseException){
            println(error)
            return 0
        }
    }

    private fun updateUserClaims(userId: String, role: String) {
        val claims: MutableMap<String, Any> = HashMap()
        if (role == "0") {
            claims["admin"] = true
        } else if (role == "1") {
            claims["roaster"] = true
        } else if (role == "2"){
            claims["customer"] = true
        }
        FirebaseAuth.getInstance().setCustomUserClaims(userId, claims)
    }

    fun getAllUsers(): List<User> {
        // Get list of Users
        var usersPage = FirebaseAuth.getInstance().listUsers(null)

        // Create List to save ids.
        val userIds = mutableListOf<String>()
        while (usersPage != null) {
            usersPage.values.forEach {
                userIds.add(it.uid)
            }
            usersPage = usersPage.nextPage
        }

        // Get Data out of Firestore based on the userIds
        return firebaseConfig.getDb().collection("users")
                .whereIn(FieldPath.documentId(), userIds).get().get()
                .documents.map { document ->
            User(document.id, document.data["firstName"] as String, document.data["lastName"] as String,
                    document.data["role"] as String, document.data["email"] as String, "", document.data["phoneNumber"] as String)
        }
    }
}
