package com.backend.koffiechefs.controller

import com.backend.koffiechefs.model.Cart
import com.backend.koffiechefs.model.Payment
import com.backend.koffiechefs.model.Transaction
import com.backend.koffiechefs.service.PaymentService
import com.google.firebase.FirebaseException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController

class PaymentController(private val paymentService: PaymentService){

    @GetMapping("/payment/transaction")
    fun getTransactions():List<Transaction>{
        return try {
            paymentService.getAllTransactions()
        }catch (e: FirebaseException){
            println("Couldn't get all transactions" + e.message)
            emptyList()
        }
    }


    @PostMapping("/payment")
    fun finishTransaction(@RequestBody payment: Payment): ResponseEntity<Unit> {

        val request = paymentService.finishTransaction(payment);
        return if(request == 1){
            ResponseEntity.status(HttpStatus.ACCEPTED).build()
        }else{
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
        }
    }

    @PostMapping("/payment/transaction")
    fun saveTransaction(@RequestBody transaction: Transaction): ResponseEntity<Unit>{
        println("Got it.")
        val request = paymentService.saveTransaction(transaction.cart,transaction.email, transaction.amount)
        return if(request == 1){
            ResponseEntity.status(HttpStatus.ACCEPTED).build()
        }else{
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
        }
    }
}