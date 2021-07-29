package com.backend.koffiechefs.controller

import com.backend.koffiechefs.model.Roaster
import com.backend.koffiechefs.model.User
import com.backend.koffiechefs.service.RoasterService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController


class RoasterController(private val roasterService:RoasterService) {

    @PostMapping("/roaster")
    fun addApplication(@RequestBody roaster: Roaster): ResponseEntity<Unit> {
        val request = roasterService.createApplication(roaster)
        return if(request == 1){
            ResponseEntity.status(HttpStatus.CREATED).build()
        }else{
            ResponseEntity.status(HttpStatus.CONFLICT).build()
        }
    }

    @PostMapping("/roaster/decline")
    fun declineApplication(@RequestBody roaster: Roaster):ResponseEntity<Unit>{
        val request = roasterService.declineApplication(roaster)
        return if(request == 1){
            ResponseEntity.status(HttpStatus.OK).build()
        }else{
            ResponseEntity.status(HttpStatus.CONFLICT).build()
        }
    }

    @GetMapping("/roaster")
    fun getAllApplications():List<Roaster> {
        return roasterService.getAllApplications()
    }
}