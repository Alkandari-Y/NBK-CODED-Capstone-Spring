package com.project.notification.controllers

import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message
import com.google.firebase.messaging.Notification
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/v1/here")
class HelloController(
    private val firebaseMessaging: FirebaseMessaging
){


    @GetMapping
    fun hello() = "bashayer at coded triggering gps"

    @PostMapping("/test")
    fun sendTestNotification(): ResponseEntity<String> {
            val message = Message.builder()
                .setToken("fake_token_for_dry_run")
                .putData("title", "Test")
                .putData("body", "Hello from Spring Boot")
                .build()

            val response = firebaseMessaging.send(message, true)
            return ResponseEntity.ok("Sent (dry run): $response")
    }
}