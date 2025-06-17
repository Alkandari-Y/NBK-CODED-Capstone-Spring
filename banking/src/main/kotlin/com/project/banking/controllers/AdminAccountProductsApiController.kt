package com.project.banking.controllers

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/admin/accounts")
class AdminAccountsApiController {

    @GetMapping
    fun getAccountProducts(): String {
        return "hello"
    }
}