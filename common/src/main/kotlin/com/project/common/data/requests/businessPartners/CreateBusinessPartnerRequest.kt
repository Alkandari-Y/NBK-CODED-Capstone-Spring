package com.project.common.data.requests.businessPartners

import jakarta.validation.constraints.NotBlank
import org.jetbrains.annotations.NotNull

data class CreateBusinessPartnerRequest(

    @field:NotNull
    val categoryId: Long,


    @field:NotBlank
    val name: String,
)