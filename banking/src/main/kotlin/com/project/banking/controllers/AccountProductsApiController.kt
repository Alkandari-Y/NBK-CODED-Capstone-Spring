package com.project.banking.controllers

import com.project.banking.entities.AccountProductEntity
import com.project.banking.entities.PerkEntity
import com.project.banking.services.AccountProductService
import com.project.banking.services.PerkService
import com.project.common.data.responses.perks.PerkDto
import com.project.common.exceptions.accountProducts.AccountProductNotFoundException
import com.project.common.exceptions.perk.PerkNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/products")
class AccountProductsApiController(
    private val accountProductService: AccountProductService,
    private val perkService: PerkService
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


    @GetMapping("/perks/{perkId}")
    fun getPerkById(
        @PathVariable perkId: Long
    ): ResponseEntity<PerkDto> {
        val perk = perkService.getPerkById(perkId)
            ?: throw PerkNotFoundException()
        return ResponseEntity(perk, HttpStatus.OK)
    }

    @GetMapping("/{productId}/perks")
    fun getAllPerksByAccountProduct(
        @PathVariable productId: Long
    ): ResponseEntity<List<PerkDto>> {
        val perks = perkService.getAllPerksByAccountProduct(productId)
        return ResponseEntity(perks, HttpStatus.OK)
    }
}