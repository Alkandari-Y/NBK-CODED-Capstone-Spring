package com.project.banking.controllers

import com.project.banking.services.BusinessPartnerService
import com.project.banking.mappers.toDto
import com.project.common.data.responses.businessPartners.BusinessPartnerDto
import com.project.common.exceptions.businessPartners.BusinessPartnerNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/v1/partners")
class BusinessPartnersApiController(
    private val businessPartnerService: BusinessPartnerService
) {

    @GetMapping
    fun getAllPartners(): List<BusinessPartnerDto> = businessPartnerService.getAllPartners().map { it.toDto() }

    @GetMapping("/{partnerId}")
    fun getPartnerById(
        @PathVariable("partnerId") id: Long
    ): ResponseEntity<BusinessPartnerDto> {
        val businessPartner = businessPartnerService.getPartnerById(id)
            ?: throw BusinessPartnerNotFoundException()

        return ResponseEntity(businessPartner.toDto(), HttpStatus.OK)
    }
}