package com.backend.koffiechefs.model

import com.google.gson.annotations.SerializedName

// Roles:   0 = Admin, 1 = Roaster, 2 = Customer

data class User(@SerializedName("id") val id: String = "",
                @SerializedName("firstName") val firstName: String,
                @SerializedName("lastName") val lastName: String,
                @SerializedName("role") val role:String,
                @SerializedName("email") val email:String,
                @SerializedName("password") val password:String = "",
                @SerializedName("phoneNumber") val phoneNumber:String)
