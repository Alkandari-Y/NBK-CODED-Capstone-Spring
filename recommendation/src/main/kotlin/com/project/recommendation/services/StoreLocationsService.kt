package com.project.recommendation.services

import com.project.recommendation.entities.StoreLocationEntity

interface StoreLocationsService {
    fun getAllStoreLocations(): Collection<StoreLocationEntity>
    fun getAllStoreLocationsByBusinessId(businessId: String?): Collection<StoreLocationEntity>

    // will need to setup redis based caching
    // fun getAllStoreLocationsByBusinessName(name: String): Collection<StoreLocationEntity>
    fun findStoreLocationById(): StoreLocationEntity?
    fun createStoreLocation(name: String): StoreLocationEntity
    fun deleteStoreLocationById(name: String)

}