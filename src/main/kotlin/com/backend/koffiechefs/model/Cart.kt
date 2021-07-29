package com.backend.koffiechefs.model

data class Cart(val amount: Int,
                val grind: String,
                val image: String,
                val itemName: String,
                val parent:String,
                val price: Int,
                val sellerName: String,
                val weight: Int)
