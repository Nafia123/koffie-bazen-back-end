package com.backend.koffiechefs.service

import com.backend.koffiechefs.config.FirebaseConfig
import com.backend.koffiechefs.model.Cart
import com.backend.koffiechefs.model.Payment
import com.braintreegateway.BraintreeGateway
import com.braintreegateway.Environment
import com.braintreegateway.Result
import com.braintreegateway.Transaction as BraintreeTransaction
import com.braintreegateway.TransactionRequest
import com.backend.koffiechefs.model.Transaction
import com.google.cloud.firestore.FirestoreException
import com.google.firebase.FirebaseException
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.util.*

@Service
class PaymentService (private val firebaseConfig: FirebaseConfig){
    companion object Gateway{
        var token = BraintreeGateway(
                Environment.SANDBOX,
        "ftywny63t6fmtpnt",
        "6hkmrjrc9ghnvwg8",
        "e4721cc117d7d70efc3a215c21967e13"
        );
    }

    fun getAllTransactions():List<Transaction>{
        return try {
            val collection = firebaseConfig.getDb().collection("transactions")
            collection.get().get().documents.map { transaction ->
                val amount = transaction.data["amount"] as Long
                Transaction(getOrdersInTransaction(transaction.id), transaction.data["orderEmail"] as String, amount.toInt())
            }
        }catch (e: FirebaseException){
            emptyList()
        }

    }

    private fun getOrdersInTransaction(id: String): List<Cart>{
        val collection = firebaseConfig.getDb().collection("transactions").document(id).collection("orders")
        return collection.get().get().documents.map { order ->
            // Fire Store stores all numbers as long. Convert to Int
            val amount = order.data["amount"] as Long
            val price = order.data["price"] as Long
            val weight = order.data["weight"] as Long
            Cart(
                    amount = amount.toInt(),
                    grind = order.data["grind"] as String,
                    image = order.data["image"] as String,
                    itemName = order.data["itemName"] as String,
                    parent = order.data["parent"] as String,
                    price = price.toInt(),
                    sellerName = order.data["sellerName"] as String,
                    weight = weight.toInt()
            )
        }
    }

    fun finishTransaction(payment: Payment):Int {
        val request: TransactionRequest = TransactionRequest()
                .amount(BigDecimal(payment.amount))
                .paymentMethodNonce(payment.nonce)
                .options()
                .submitForSettlement(true).done()

        val result: Result<BraintreeTransaction> = Gateway.token.transaction().sale(request)
        return if (result.isSuccess) 1
        else 0

    }

    fun saveTransaction(cart: List<Cart>, email: String, amount: Number):Int{
        try {
            val listOfParentIds = mutableListOf<String>()
            cart.forEach { cartItem -> listOfParentIds.add(cartItem.parent) }
            val db = firebaseConfig.getDb()
            val transactionDoc = db.collection("transactions").document()
            val transactionFields = hashMapOf(
                    "amount" to amount,
                    "date" to Date(),
                    "orderEmail" to email
            )
            transactionDoc.set(transactionFields as Map<String, Any>)

            cart.forEach { cartItem ->
                addAmountToRoaster(cartItem)
                transactionDoc.collection("orders").add(cartItem)
            }


            return 1
        }catch (e: FirestoreException){
            println("Transaction couldn't be saved " + e.message)
            return 0
        }
    }

    private fun addAmountToRoaster(cartItem: Cart){
        val roasterData = firebaseConfig.getDb().collection("users").whereEqualTo("companyName",cartItem.sellerName).get().get().documents[0]
        val totalPrice = cartItem.price * cartItem.amount
        val amount = roasterData.data["amount"] as Long + totalPrice
        hashMapOf("amount" to amount)
        firebaseConfig.getDb().collection("users").document(roasterData.id).update(hashMapOf("amount" to amount) as Map<String,Any>)
    }


}