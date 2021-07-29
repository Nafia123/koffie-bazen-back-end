package com.backend.koffiechefs.model

data class Transaction(val cart: List<Cart>, val email: String, val amount: Int)