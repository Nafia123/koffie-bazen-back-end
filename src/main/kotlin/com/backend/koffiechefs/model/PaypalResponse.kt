package com.backend.koffiechefs.model

data class PaypalResponse (val nonce: String, val payerId: String, val email: String, val firstName: String, val lastName: String, val phone:String)