package com.project.banking.controllers

import com.project.banking.services.XpTierService
import com.project.common.data.responses.xp.XpTierResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/xp-tiers")
class XpApiController(
    private val xpTierService: XpTierService
) {
    @GetMapping
    fun getAllTiers(): ResponseEntity<List<XpTierResponse>> {
        val tiers = xpTierService.getAllTiers()
        return ResponseEntity(tiers, HttpStatus.OK)
    }

    @GetMapping("/{id}")
    fun getTierById(@PathVariable id: Long): ResponseEntity<XpTierResponse> {
        val tier = xpTierService.getXpTierById(id)
        return ResponseEntity(tier, HttpStatus.OK)
    }
}