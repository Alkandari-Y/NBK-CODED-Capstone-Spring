package com.project.common.data.requests.geofencing

import jakarta.validation.constraints.NotBlank


data class LatLng(
    @field:NotBlank
    val latitude: Double,
    @field:NotBlank
    val longitude: Double
)