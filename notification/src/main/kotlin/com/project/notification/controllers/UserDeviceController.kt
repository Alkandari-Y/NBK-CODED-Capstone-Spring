package com.project.notification.controllers

import com.project.common.data.requests.firebase.UserDeviceFBTokenRequest
import com.project.common.data.responses.authentication.UserInfoDto
import com.project.notification.services.UserDeviceService
import jakarta.validation.constraints.NotBlank
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/device")
class UserDeviceController(
    private val userDeviceService: UserDeviceService
) {

    @PostMapping("/register")
    fun registerFirebaseToken(
        @RequestAttribute("authUser") authUser: UserInfoDto,
        @RequestBody request: UserDeviceFBTokenRequest
    ): ResponseEntity<Void> {
        userDeviceService.registerFirebaseToken(
            userId = authUser.userId,
            fbTokenRequest = request)
        return ResponseEntity.ok().build()
    }
}
