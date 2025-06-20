package com.project.banking.controllers

import com.project.banking.mappers.toDto
import com.project.banking.services.BusinessPartnerService
import com.project.common.data.responses.businessPartners.BusinessPartnerDto
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/v1/partners")
class BusinessPartnersApiController(
    private val businessPartnerService: BusinessPartnerService
) {

    @GetMapping
    fun getAllPartners(): List<BusinessPartnerDto> = businessPartnerService.getAllPartners().map { it.toDto() }
}