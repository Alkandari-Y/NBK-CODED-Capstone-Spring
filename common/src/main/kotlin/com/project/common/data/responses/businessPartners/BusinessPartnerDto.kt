package com.project.common.data.responses.businessPartners

import com.project.common.data.responses.categories.CategoryDto

data class BusinessPartnerDto(
    val id: Long,
    val name: String,
    val logoUrl: String,
    val category: CategoryDto,
)
