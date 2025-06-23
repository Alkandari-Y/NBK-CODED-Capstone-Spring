package com.project.banking.controllers

import com.project.banking.services.XpService
import com.project.banking.services.XpTierService
import com.project.common.data.responses.xp.UserXpInfoResponse
import com.project.common.data.responses.xp.XpTierResponse
import com.project.common.security.RemoteUserPrincipal
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/xp")
class XpApiController(
    private val xpTierService: XpTierService,
    private val xpService: XpService
) {
    @GetMapping("/tiers")
    fun getAllTiers(): ResponseEntity<List<XpTierResponse>> {
        val tiers = xpTierService.getAllTiers()
        return ResponseEntity(tiers, HttpStatus.OK)
    }

    @GetMapping("/tiers/{id}")
    fun getTierById(@PathVariable id: Long): ResponseEntity<XpTierResponse> {
        val tier = xpTierService.getXpTierById(id)
        return ResponseEntity(tier, HttpStatus.OK)
    }

    @GetMapping
    fun getUserXpInfo(
        @AuthenticationPrincipal user: RemoteUserPrincipal
    ): ResponseEntity<UserXpInfoResponse> {
        val body = xpService.getCurrentXpInfo(user.getUserId())
        return ResponseEntity(body, HttpStatus.OK)
    }
}