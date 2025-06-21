package com.project.common.data.requests.businessPartners

import jakarta.validation.constraints.*

data class CreateBusinessPartnerRequest(
    @field:NotBlank
    val name: String,

    @field:NotNull
    val categoryId: Long,

    @field:NotBlank
    val logoUrl: String,
)