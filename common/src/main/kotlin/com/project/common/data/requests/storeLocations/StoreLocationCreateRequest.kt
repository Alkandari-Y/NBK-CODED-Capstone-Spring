package com.project.common.data.requests.storeLocations

data class StoreLocationCreateRequest (
    val partnerId: Long,
    val longitude: Any,
    val latitude: Any,
    val googleMapsUrl: String,
    val country: String,
    val addressLine1: String,
    val addressLine2: String,
    val opensAt: String,
    val closesAt: String,
    val beaconId: Long,
)