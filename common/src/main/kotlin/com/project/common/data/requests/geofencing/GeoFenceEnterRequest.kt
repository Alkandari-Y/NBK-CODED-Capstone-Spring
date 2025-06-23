package com.project.common.data.requests.geofencing

import com.project.common.enums.LocationType
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull


data class GeoFenceEnterRequest(
    @field:NotNull
    val id: String,

    @field:NotBlank
    val name: String,

    @field:NotNull
    val location: LatLng,

    @field:NotNull
    val radius: Float = 300f,

    @field:NotNull
    val type: LocationType = LocationType.MALL,
    @field:NotBlank
    val description: String = "",

    val tags: List<String> = emptyList(),
    var geofenceId: String?
)