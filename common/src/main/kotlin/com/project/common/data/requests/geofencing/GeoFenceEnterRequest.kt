package com.project.common.data.requests.geofencing

import com.project.common.enums.LocationType


data class GeoFenceEnterRequest(
    val id: String,
    val name: String,
    val location: LatLng,
    val radius: Float = 300f,
    val type: LocationType = LocationType.MALL,
    val description: String = "",
    val tags: List<String> = emptyList(),
//    var geofenceId: String?
)