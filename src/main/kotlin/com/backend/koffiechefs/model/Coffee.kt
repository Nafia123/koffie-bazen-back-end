package com.backend.koffiechefs.model

data class Coffee(val id: String?,
                  val itemName: String,
                  val sellerName: String,
                  val sellerId: String,
                  val image: String,
                  val description: String,
                  val originLocation: String,
                  val farmer: String,
                  val height: String,
                  val washingProcess: String,
                  val options: List<CoffeeOption>?)
