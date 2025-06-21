package com.project.common.data.requests.businessPartners

import jakarta.validation.constraints.Min
import org.jetbrains.annotations.NotNull

data class FavBusinessRequest(
    @field:NotNull
    @field:Min(1)
    var partnerId: Long
)
