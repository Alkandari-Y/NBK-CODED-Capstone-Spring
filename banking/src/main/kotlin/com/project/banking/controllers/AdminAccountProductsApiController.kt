package com.project.banking.controllers

import com.project.banking.entities.AccountProductEntity
import com.project.banking.services.AccountProductService
import com.project.common.data.requests.accountProducts.CreateAccountProductRequest
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

@RestController
@RequestMapping("/api/v1/admin/products")
class AdminAccountProductsApiController(
    private val accountProductService: AccountProductService,
) {
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(consumes = ["multipart/form-data"])
    fun createAccountProducts(
        @Valid @ModelAttribute accountProductCreateRequest: CreateAccountProductRequest,
        @RequestPart("image", required = true) image: MultipartFile,
    ): ResponseEntity<AccountProductEntity> {
        val allowedTypes = listOf("image/jpeg", "image/png", "image/jpg", "image/webp")
        if (image.contentType !in allowedTypes) {
            throw IllegalArgumentException("Only JPEG, PNG, or WEBP images are allowed.")
        }

        if (image.contentType !in allowedTypes) {
            throw IllegalArgumentException("Only JPEG, PNG, or WEBP images are allowed.")
        }

        val accountProduct = accountProductService.createNewAccountProduct(image, accountProductCreateRequest)

        return ResponseEntity(accountProduct, HttpStatus.CREATED)
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{productId}")
    fun deleteAccountProductById(
        @PathVariable("productId") productId: Long
    ): ResponseEntity<Void> {
        accountProductService.deleteAccountProductById(productId)
        return ResponseEntity.noContent().build()
    }
}