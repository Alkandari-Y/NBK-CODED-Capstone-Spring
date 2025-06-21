package com.project.common.data.responses.storeLocations

data class StoreLocationResponse(
    val id: Long?,
    val partnerId: Long?,
    val longitude: Double?,
    val latitude: Double?,
    val googleMapUrl: String,
    val country: String,
    val addressLine1: String,
    val addressLine2: String?,
    val opensAt: String,
    val closesAt: String,
    val beaconId: Long,
    val location: String?
)