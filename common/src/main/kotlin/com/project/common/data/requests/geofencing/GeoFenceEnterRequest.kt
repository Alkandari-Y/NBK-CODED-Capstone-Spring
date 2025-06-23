package com.project.common.data.requests.geofencing

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive


data class GeofenceEventRequest(
    @field:NotNull
    val id: String,
    @field:NotBlank
    val name: String,
    @field:NotNull
    val latitude: Double,
    @field:NotNull
    val longitude: Double,
    @field:NotNull
    val radius: Float,
    @field:NotBlank
    val type: String,

    val description: String?,
    val tags: List<String>?,
    @field:NotBlank
    val transitionType: String, // "ENTER" or "EXIT"

    @field:NotNull
    @field:Positive
    val userId: Long
)