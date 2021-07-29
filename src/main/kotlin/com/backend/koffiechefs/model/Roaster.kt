package com.backend.koffiechefs.model

import com.google.gson.annotations.SerializedName


data class Roaster(
                   @SerializedName("id") val id: String = "",
                   @SerializedName("firstName") val firstName: String,
                   @SerializedName("lastName") val lastName: String,
                   @SerializedName("email") val email:String,
                   @SerializedName("password") val password:String,
                   @SerializedName("phoneNumber") val phoneNumber:String,
                   @SerializedName("address") val address: String,
                   @SerializedName("zipcode") val zipcode: String,
                   @SerializedName("companyName") val companyName: String,
                   @SerializedName("dateSent") val dateSent: String = "",
                   @SerializedName("amount") val amount: Int = 0)
