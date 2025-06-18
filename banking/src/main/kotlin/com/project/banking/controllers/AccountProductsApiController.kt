package com.project.banking.controllers

import com.project.banking.entities.AccountProductEntity
import com.project.banking.services.AccountProductService
import com.project.common.exceptions.accountProducts.AccountProductNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/products")
class AccountProductsApiController(
    private val accountProductService: AccountProductService
) {

    @GetMapping
    fun getAccountProducts(): ResponseEntity<List<AccountProductEntity>> =
        ResponseEntity(
            accountProductService.getAllAccountProducts(),
            HttpStatus.OK
        )


    @GetMapping("/{productId}")
    fun getAccountProductDetails(
        @PathVariable("productId") productId: Long
    ): ResponseEntity<AccountProductEntity> {
        val accountProduct = accountProductService.getAccountProductById(productId)
            ?: throw AccountProductNotFoundException()
        return ResponseEntity(accountProduct, HttpStatus.OK)
    }



}