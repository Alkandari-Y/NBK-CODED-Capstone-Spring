package com.project.recommendation.repositories

import com.project.recommendation.entities.StoreLocationEntity
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface StoreLocationRepository {
    @Query(
        value = """
        SELECT * FROM store_locations
        WHERE ST_DWithin(location, ST_SetSRID(ST_MakePoint(:lon, :lat), 4326), :radius)
        ORDER BY ST_Distance(location, ST_SetSRID(ST_MakePoint(:lon, :lat), 4326))
    """,
        nativeQuery = true
    )
    fun findNearbyStoresByGeofencing(
        @Param("lon") longitude: Double,
        @Param("lat") latitude: Double,
        @Param("radius") radiusInMeters: Double
    ): List<StoreLocationEntity>}