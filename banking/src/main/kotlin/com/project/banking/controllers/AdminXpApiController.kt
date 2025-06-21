package com.project.banking.controllers

import com.project.banking.services.XpTierService
import com.project.common.data.requests.xp.CreateXpTierRequest
import com.project.common.data.responses.xp.XpTierResponse
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/admin/xp-tiers")
class AdminXpApiController(
    private val xpTierService: XpTierService
) {
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    fun createXpTier(@Valid @RequestBody request: CreateXpTierRequest): ResponseEntity<XpTierResponse> {
        val body = xpTierService.createXpTier(request)
        return ResponseEntity(body, HttpStatus.CREATED)
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    fun deleteXpTier(@PathVariable id: Long): ResponseEntity<Void> {
        xpTierService.deleteXpTierById(id)
        return ResponseEntity.noContent().build()
    }
}