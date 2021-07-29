package com.backend.koffiechefs.controller

import com.backend.koffiechefs.service.CustomerService
import org.springframework.web.bind.annotation.RestController


@RestController
class CustomerController(private val customerService: CustomerService) {


}