package com.project.notification.controllers

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/v1/here")
class HelloController {


    @GetMapping
    fun hello() = "bashayer at coded triggering gps"
}