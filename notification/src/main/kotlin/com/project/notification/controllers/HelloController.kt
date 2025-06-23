package com.project.notification.controllers

import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message
import com.google.firebase.messaging.Notification
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
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
    fun sendTestNotification(@Valid @RequestBody firebaseToken: FirebaseToken): ResponseEntity<String> {
            val message = Message.builder()
                .setToken(firebaseToken.token)
                .putData("title", "Test")
                .putData("body", "Hello from Spring Boot")
                .putData("customKey", "customValue")
                .setNotification(
                    Notification.builder()
                        .setTitle("Test")
                        .setBody("Hello from Spring Boot")
                        .build()
                )
                .build()

            val response = firebaseMessaging.send(message)
            return ResponseEntity.ok("Sent: $response")
    }
}

data class FirebaseToken(
    @field:NotBlank
    val token: String
)