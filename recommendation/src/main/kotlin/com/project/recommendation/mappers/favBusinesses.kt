package com.project.recommendation.mappers

import com.project.common.data.requests.businessPartners.FavBusinessesRequest
import com.project.common.data.responses.businessPartners.FavoriteBusinessDto
import com.project.common.data.responses.businessPartners.FavoriteBusinessesResponse
import com.project.recommendation.entities.FavBusinessEntity

fun FavBusinessEntity.toDto(): FavoriteBusinessDto {
    return FavoriteBusinessDto(
        id = this.id!!,
        userId = this.userId!!,
        partnerId = this.partnerId!!
    )
}

fun List<FavBusinessEntity>.toFavoriteBusinessResponse(): FavoriteBusinessesResponse {
    return FavoriteBusinessesResponse(
        favBusinesses = this.map { it.toDto() }
    )
}

fun FavBusinessesRequest.toEntityList(userId: Long): List<FavBusinessEntity> {
    return this.partnerIds.map {
        FavBusinessEntity(userId = userId, partnerId = it
        )
    }
}

