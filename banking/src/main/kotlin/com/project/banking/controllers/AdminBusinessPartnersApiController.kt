package com.project.banking.controllers

import com.project.banking.entities.AccountEntity
import com.project.banking.mappers.toDto
import com.project.banking.services.BusinessPartnerService
import com.project.common.data.requests.businessPartners.CreateBusinessPartnerRequest
import com.project.common.data.responses.authentication.UserInfoDto
import com.project.common.data.responses.businessPartners.BusinessPartnerDto
import com.project.common.data.responses.businessPartners.PartnerAccountResponse
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestAttribute
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/v1/admin/partners")
class AdminBusinessPartnersApiController(
    private val businessPartnerService: BusinessPartnerService
) {
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    fun createPartner(
        @Valid @RequestBody createBusinessPartnerRequest: CreateBusinessPartnerRequest,
        @RequestAttribute("authUser") authUser: UserInfoDto,
        ): ResponseEntity<BusinessPartnerDto> {
        val businessPartner = businessPartnerService.createBusinessPartner(createBusinessPartnerRequest, authUser)
        return ResponseEntity(
            businessPartner.toDto(),
            HttpStatus.CREATED
        )
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{partnerId}")
    fun deletePartner(
        @PathVariable(value = "partnerId") partnerId: Long
    ):  ResponseEntity<Void> {
        businessPartnerService.deletePartnerById(partnerId)
        return ResponseEntity.noContent().build()
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("{partnerId}/account")
    fun getPartnerAccount(
        @PathVariable partnerId: Long
    ): ResponseEntity<PartnerAccountResponse> {
        val accountNumber = businessPartnerService.getPartnerAccount(partnerId)
        return ResponseEntity.ok(accountNumber)
    }
}