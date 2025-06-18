package com.project.banking.controllers

import com.project.banking.services.BusinessPartnerService
import com.project.common.data.requests.businessPartners.CreateBusinessPartnerRequest
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import kotlin.collections.contains


//@RestController
//@RequestMapping("/api/v1/admin/partners")
//class AdminBusinessPartnersApiController(
//    private val businessPartnerService: BusinessPartnerService
//) {
//
//
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
//    @PostMapping(consumes = ["multipart/form-data"])
//    fun createPartner(
//        @Valid @ModelAttribute createBusinessPartnerRequest: CreateBusinessPartnerRequest,
//        @RequestPart("image", required = true) image: MultipartFile,
//    ): ResponseEntity<String> {
//        val allowedTypes = listOf("image/jpeg", "image/png", "image/jpg", "image/webp")
//        if (image.contentType !in allowedTypes) {
//            throw IllegalArgumentException("Only JPEG, PNG, or WEBP images are allowed.")
//        }
//
//        if (image.contentType !in allowedTypes) {
//            throw IllegalArgumentException("Only JPEG, PNG, or WEBP images are allowed.")
//        }
//
//        return ResponseEntity("", HttpStatus.CREATED)
//    }
//
//
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
//    @DeleteMapping
//    fun deletePartner(
//        @PathVariable(value = "partnerId") partnerId: Long
//    ):  ResponseEntity<Void> {
//        businessPartnerService
//        return ResponseEntity.noContent().build()
//    }
//
//}