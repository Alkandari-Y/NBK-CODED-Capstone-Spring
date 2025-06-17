package com.project.recommendation.entities

import jakarta.persistence.*
import java.time.LocalTime
import org.locationtech.jts.geom.Point
import java.math.BigDecimal

@Entity
@Table(name = "store_locations")
data class StoreLocationEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    var id: Long? = null,

    @Column(name = "partner_id", nullable = false)
    var partnerId: Long? = null,

    @Column(name = "longitude", nullable = false)
    var longitude: Double? = null,

    @Column(name = "latitude", nullable = false)
    var latitude: Double? = null,

    @Column(name = "google_map_url", nullable = false)
    var googleMapUrl: String,

    @Column(name = "country", nullable = false)
    var country: String,

    @Column(name = "address_line_1", nullable = false)
    var addressLine1: String,

    @Column(name = "address_line_2")
    var addressLine2: String? = null,

    @Column(name = "opens_at", nullable = false)
    var opensAt: LocalTime,

    @Column(name = "closes_at", nullable = false)
    var closesAt: LocalTime,

    @Column(name = "beacon_id", nullable = false)
    var beaconId: Long,

    @Column(name = "location", columnDefinition = "geography(Point,4326)", insertable = false, updatable = false)
    var location: Point? = null
)
