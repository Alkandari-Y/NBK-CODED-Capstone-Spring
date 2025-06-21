package com.project.recommendation.services

import com.project.common.data.requests.businessPartners.FavBusinessRequest
import com.project.common.data.requests.businessPartners.FavBusinessesRequest
import com.project.recommendation.entities.FavBusinessEntity

interface FavBusinessService {
    fun findAllFavBusinesses(userId: Long): List<FavBusinessEntity>
    fun setAllFavBusinesses(favBusinessRequest: FavBusinessesRequest, userId: Long): List<FavBusinessEntity>
    fun addOneFavBusiness(favBusinessRequest: FavBusinessRequest, userId: Long): FavBusinessEntity
    fun removeFavBusinesses(userId: Long, favBusinessesRequest: FavBusinessesRequest)
    fun removeOneFavBusiness(userId: Long, favBusinessRequest: FavBusinessRequest)
    fun clearAllFavBusinesses(userId: Long)
}