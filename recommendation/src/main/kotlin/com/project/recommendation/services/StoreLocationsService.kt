package com.project.recommendation.services

import com.project.common.data.requests.storeLocations.StoreLocationCreateRequest
import com.project.recommendation.entities.StoreLocationEntity

interface StoreLocationsService {
    fun getAllStoreLocations(): Collection<StoreLocationEntity>
    fun getAllStoreLocationsByBusinessId(businessId: Long): Collection<StoreLocationEntity>
    fun findStoreLocationById(storeId: Long): StoreLocationEntity?
    fun createStoreLocation(newLocation: StoreLocationCreateRequest): StoreLocationEntity
    fun deleteStoreLocationById(storeLocationId: Long)

}