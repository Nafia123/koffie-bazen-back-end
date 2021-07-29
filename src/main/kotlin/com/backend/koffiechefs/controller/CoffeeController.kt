package com.backend.koffiechefs.controller

import com.backend.koffiechefs.model.Coffee
import com.backend.koffiechefs.service.CoffeeService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException


@RestController
class CoffeeController(private val coffeeService: CoffeeService) {

    @GetMapping("/coffees/{id}")
    fun getCoffees(@PathVariable id: String): Coffee {
        try {
            return coffeeService.getCoffee(id)
        } catch (exception: Exception) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find Coffee", exception)
        }
    }

    @GetMapping("/coffees")
    fun getAllCoffees(@RequestParam(required = false) sellerId: String?): List<Coffee> {
        try {
            return coffeeService.getCoffees(sellerId)
        } catch (exception: Exception) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find Coffees", exception)
        }
    }

    @PostMapping("/coffees")
    fun addCoffee(@RequestBody coffee: Coffee){
        return coffeeService.addCoffee(coffee)
    }

    @PutMapping("/coffees/{id}")
    fun updateCoffee(@PathVariable id: String, @RequestBody coffee: Coffee) {
        return coffeeService.updateCoffee(id, coffee)
    }

    @DeleteMapping("/coffees/{id}")
    fun deleteCoffee(@PathVariable id: String){
        return coffeeService.deleteCoffee(id)
    }
}
