package com.project.common.data.responses.businessPartners

data class BusinessPartnerSummaryDto(
    val id: Long,
    val name: String,
    val logoUrl: String,
    val categoryId: Long,
)