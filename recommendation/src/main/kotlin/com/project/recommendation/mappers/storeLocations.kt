package com.project.recommendation.mappers

import com.project.common.data.requests.storeLocations.StoreLocationCreateRequest
import com.project.common.data.responses.storeLocations.StoreLocationResponse
import com.project.common.utils.parseCoordinate
import com.project.common.utils.timeFormatter
import com.project.recommendation.entities.StoreLocationEntity
import java.time.LocalTime
import org.locationtech.jts.geom.Point
import org.locationtech.jts.io.WKTWriter

typealias JtsPoint = org.locationtech.jts.geom.Point

fun Point.toText(): String = WKTWriter().write(this)

fun StoreLocationCreateRequest.toEntity(): StoreLocationEntity {
    return StoreLocationEntity(
        partnerId = partnerId,
        longitude = parseCoordinate(longitude),
        latitude = parseCoordinate(latitude),
        googleMapUrl = googleMapsUrl,
        country = country,
        addressLine1 = addressLine1,
        addressLine2 = addressLine2,
        opensAt = LocalTime.parse(opensAt, timeFormatter),
        closesAt = LocalTime.parse(closesAt, timeFormatter),
        beaconId = beaconId
    )
}

fun StoreLocationEntity.toResponse(): StoreLocationResponse {
    return StoreLocationResponse(
        id = id,
        partnerId = partnerId,
        longitude = longitude.toDouble(),
        latitude = latitude.toDouble(),
        googleMapUrl = googleMapUrl,
        country = country,
        addressLine1 = addressLine1,
        addressLine2 = addressLine2,
        opensAt = opensAt.toString(),
        closesAt = closesAt.toString(),
        beaconId = beaconId,
        location  = (location as? JtsPoint)?.toText()

    )
}