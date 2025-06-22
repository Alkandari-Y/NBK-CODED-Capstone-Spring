package com.project.recommendation.services

import com.project.common.data.requests.geofencing.GeoFenceEnterRequest
import com.project.common.data.requests.storeLocations.StoreLocationCreateRequest
import com.project.common.data.responses.storeLocations.StoreLocationResponse
import com.project.common.exceptions.storeLocations.StoreLocationNotFoundException
import com.project.recommendation.entities.StoreLocationEntity
import com.project.recommendation.mappers.toEntity
import com.project.recommendation.mappers.toResponse
import com.project.recommendation.providers.BankServiceProvider
import com.project.recommendation.repositories.StoreLocationRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service


@Service
class StoreLocationsServiceImpl(
    private val storeLocationRepository: StoreLocationRepository,
    private val bankServiceProvider: BankServiceProvider

): StoreLocationsService {
    override fun getAllStoreLocations(): Collection<StoreLocationEntity> {
        return storeLocationRepository.findAll()
    }

    override fun getAllStoreLocationsByBusinessId(businessId: Long): Collection<StoreLocationEntity> {
        return storeLocationRepository.findStoresByPartnerId(businessId)
    }

    override fun findStoreLocationById(storeId: Long): StoreLocationEntity? {
        return storeLocationRepository.findByIdOrNull(storeId) ?: throw StoreLocationNotFoundException()
    }

    override fun createStoreLocation(newLocation: StoreLocationCreateRequest): StoreLocationEntity {
        bankServiceProvider.getBusinessPartner(newLocation.partnerId)
        val id = storeLocationRepository.save(newLocation.toEntity()).id
        val fullEntity = storeLocationRepository.findById(id!!).orElseThrow()

        return storeLocationRepository.save(fullEntity)
    }

    override fun deleteStoreLocationById(storeLocationId: Long) {
        storeLocationRepository.deleteById(storeLocationId)
    }

    override fun findNearbyStores(geofenceData: GeoFenceEnterRequest): List<StoreLocationResponse> {
        return storeLocationRepository.findStoresWithinGeofence(
            longitude = geofenceData.location.longitude,
            latitude = geofenceData.location.latitude,
            radius = geofenceData.radius
        ).map { it.toResponse() }
    }
}