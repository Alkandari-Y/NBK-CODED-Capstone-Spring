package com.project.notification.controllers

import com.project.common.data.requests.users.UserDeviceFBTokenRequest
import com.project.notification.services.UserDeviceService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/device")
class UserDeviceController(
    private val userDeviceService: UserDeviceService
) {
    @PostMapping("/register")
    fun registerFirebaseToken(@RequestBody request: UserDeviceFBTokenRequest): ResponseEntity<Void> {
        userDeviceService.registerFirebaseToken(request.userId, request.firebaseToken)
        return ResponseEntity.ok().build()
    }
}