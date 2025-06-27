package com.project.notification.controllers

import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message
import com.google.firebase.messaging.Notification
import com.project.common.data.requests.firebase.UserDeviceFBTokenRequest
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/v1/here")
class TestApiController(
    private val firebaseMessaging: FirebaseMessaging
){


    @GetMapping
    fun hello() = "bashayer at coded triggering gps"

    @PostMapping("/test")
    fun sendTestNotification(@Valid @RequestBody firebaseToken: UserDeviceFBTokenRequest): ResponseEntity<String> {
            val message = Message.builder()
                .setToken(firebaseToken.firebaseToken)
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
