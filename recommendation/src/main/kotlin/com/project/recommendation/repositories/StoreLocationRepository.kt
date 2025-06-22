package com.project.recommendation.repositories

import com.project.recommendation.entities.StoreLocationEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface StoreLocationRepository: JpaRepository<StoreLocationEntity, Long> {
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
    ): List<StoreLocationEntity>

    @Query(
        """
            SELECT ste 
                FROM StoreLocationEntity ste
            WHERE ste.partnerId = :partnerId
        """
    )
    fun findStoresByPartnerId(@Param("partnerId") partnerId: Long): List<StoreLocationEntity>


    @Query(
        value = """
            SELECT * 
            FROM store_locations
            WHERE ST_DWithin(
                location,
                ST_SetSRID(ST_MakePoint(:longitude, :latitude), 4326)::geography,
                :radius
            )
            ORDER BY ST_Distance(
                location,
                ST_SetSRID(ST_MakePoint(:longitude, :latitude), 4326)::geography
            )
        """,
        nativeQuery = true
    )
    fun findStoresWithinGeofence(
        @Param("longitude") longitude: Double,
        @Param("latitude") latitude: Double,
        @Param("radius") radius: Float
    ): List<StoreLocationEntity>

}