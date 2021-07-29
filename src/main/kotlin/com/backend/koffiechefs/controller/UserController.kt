package com.backend.koffiechefs.controller

import com.backend.koffiechefs.model.Roaster
import com.backend.koffiechefs.model.User
import com.backend.koffiechefs.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class UserController(private val userService: UserService) {

    @GetMapping("/users")
    fun getAllUsers():List<User> {
        return userService.getAllUsers()
    }

    @PostMapping("/users")
    fun addUser(@RequestBody user:User): ResponseEntity<Unit>{
        val request = userService.addUser(user)
        return if(request == 1){
             ResponseEntity.status(HttpStatus.CREATED).build()
        }else{
             ResponseEntity.status(HttpStatus.CONFLICT).build()
        }
    }

    @PostMapping("/users/roaster")
    fun addRoaster(@RequestBody roaster: Roaster) : ResponseEntity<Unit>{
        val request = userService.addRoaster(roaster)
        return if(request == 1){
            ResponseEntity.status(HttpStatus.CREATED).build()
        }else{
            ResponseEntity.status(HttpStatus.CONFLICT).build()
        }
    }

    @GetMapping("/users/roaster")
    fun getRoasters() : List<Roaster>{
        return userService.getAllRoaster()
    }

    @PutMapping("/users")
    fun updateUser(@RequestBody user:User){
        return userService.updateUser(user)
    }

    @DeleteMapping("/users/{id}")
    fun deleteUser(@PathVariable id:String){
        return userService.deleteUser(id)
    }

}