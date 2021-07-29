package com.backend.koffiechefs.service

import com.backend.koffiechefs.config.FirebaseConfig
import com.backend.koffiechefs.model.Coffee
import com.backend.koffiechefs.model.CoffeeOption
import org.springframework.stereotype.Service

@Service
class CoffeeService(private val firebaseConfig: FirebaseConfig) {
    fun getCoffee(id: String): Coffee {
        val document = firebaseConfig.getDb().collection("coffees").document(id)
        return Coffee(id = id,
                itemName = document.get().get().get("itemName") as String,
                sellerName = document.get().get().get("sellerName") as String,
                sellerId = document.get().get().get("sellerId") as String,
                image = document.get().get().get("image") as String,
                description = document.get().get().get("description") as String,
                originLocation = document.get().get().get("originLocation") as String,
                farmer = document.get().get().get("farmer") as String,
                height = document.get().get().get("height") as String,
                washingProcess = document.get().get().get("washingProcess") as String,
                options = getCoffeeOptions(id))
    }

    fun getCoffees(sellerId: String? = null): List<Coffee> {
        val coffeeDocumentPath = "coffees"
        // Get collection snapshot of coffees
        var coffeeCollection = firebaseConfig.getDb()
                .collection(coffeeDocumentPath)
        var querySnapshot = coffeeCollection.get()
        if (sellerId != null) {
            querySnapshot = coffeeCollection.whereEqualTo("sellerId", sellerId).get()
        }

        // Map snapshot into Coffee List
        return querySnapshot
                .get()
                .documents.map { coffee ->
                    Coffee(id = coffee.id,
                            itemName = coffee.data["itemName"] as String,
                            sellerName = coffee.data["sellerName"] as String,
                            sellerId = coffee.data["sellerId"] as String,
                            image = coffee.data["image"] as String,
                            description = coffee.data["description"] as String,
                            originLocation = coffee.data["originLocation"] as String,
                            farmer = coffee.data["farmer"] as String,
                            height = coffee.data["height"] as String,
                            washingProcess = coffee.data["washingProcess"] as String,
                            options = getCoffeeOptions(coffee.id))
                }
    }

    fun addCoffee(coffee: Coffee) {
        val userDocument = firebaseConfig.getDb().collection("users").document(coffee.sellerId)
        val document = firebaseConfig.getDb().collection("coffees").document()
        val fields = hashMapOf(
                "itemName" to coffee.itemName,
                "sellerId" to coffee.sellerId,
                "sellerName" to (userDocument.get().get().getString("firstName") + " " + userDocument.get().get().getString("lastName")),
                "image" to coffee.image,
                "description" to coffee.description,
                "originLocation" to coffee.originLocation,
                "farmer" to coffee.farmer,
                "height" to coffee.height,
                "washingProcess" to coffee.washingProcess)

        document.set(fields as Map<String, Any>)
        coffee.options?.forEach { option ->
            option.parent = document.id;
            document.collection("options").add(option)
        }
    }

    fun updateCoffee(id: String, coffee: Coffee) {
        val document = firebaseConfig.getDb().collection("coffees").document(id)
        val fields = hashMapOf(
                "itemName" to coffee.itemName,
                "sellerName" to coffee.sellerName,
                "sellerId" to coffee.sellerId,
                "image" to coffee.image,
                "description" to coffee.description,
                "originLocation" to coffee.originLocation,
                "farmer" to coffee.farmer,
                "height" to coffee.height,
                "washingProcess" to coffee.washingProcess)

        document.update(fields as Map<String, Any>)
        document.collection("options").listDocuments().forEach { optionDocument ->
            optionDocument.delete()
        }
        coffee.options?.forEach { optionDocument ->
            optionDocument.parent = document.id
            document.collection("options").add(optionDocument)
        }
    }

    fun deleteCoffee(id: String) {
        val document = firebaseConfig.getDb().collection("coffees").document(id)
        document.collection("options").listDocuments().forEach { optionDocument ->
            optionDocument.delete()
        }
        document.delete()
    }

    private fun getCoffeeOptions(documentId: String): List<CoffeeOption> {
        // Get collection snapshot of coffee options
        val coffeeOptionCollection = firebaseConfig.getDb()
                .collection("coffees")
                .document(documentId)
                .collection("options")

        // Map snapshot into CoffeeOption List
        return coffeeOptionCollection
                .get()
                .get()
                .documents.map { option ->
                    CoffeeOption(parent = documentId,
                            grind = option.data["grind"] as String,
                            price = option.data["price"] as Long,
                            weight = option.data["weight"] as Long)
                }
    }
}
