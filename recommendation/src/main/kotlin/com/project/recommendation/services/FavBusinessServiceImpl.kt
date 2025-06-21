package com.project.recommendation.services

import com.project.common.data.requests.businessPartners.FavBusinessRequest
import com.project.common.data.requests.businessPartners.FavBusinessesRequest
import com.project.common.exceptions.FavBusinessPartnersLimitException
import com.project.common.exceptions.businessPartners.FavoriteBusinessExistsForUserException
import com.project.recommendation.entities.FavBusinessEntity
import com.project.recommendation.mappers.toEntityList
import com.project.recommendation.repositories.FavBusinessRepository
import org.springframework.stereotype.Service

@Service
class FavBusinessServiceImpl(
    private val favBusinessRepository: FavBusinessRepository,
): FavBusinessService {
    private val MAX_BUSINESS_PARTNERS = 5
    override fun findAllFavBusinesses(userId: Long): List<FavBusinessEntity> {
        return favBusinessRepository.findAllByUserId(userId)
    }

    override fun setAllFavBusinesses(
        favBusinessRequest: FavBusinessesRequest,
        userId: Long
    ): List<FavBusinessEntity> {
        if (favBusinessRequest.partnerIds.size >= MAX_BUSINESS_PARTNERS) {
            throw FavBusinessPartnersLimitException()
        }

        favBusinessRepository.deleteAllByUserId(userId)

        return favBusinessRepository.saveAll(favBusinessRequest.toEntityList(userId))
    }

    override fun addOneFavBusiness(
        favBusinessRequest: FavBusinessRequest,
        userId: Long
    ): FavBusinessEntity {
        val currentFavs = favBusinessRepository.findAllByUserId(userId)
        if (currentFavs.size >= MAX_BUSINESS_PARTNERS) { throw FavBusinessPartnersLimitException() }


        if (favBusinessRepository.existsByUserIdAndPartnerId(userId, favBusinessRequest.partnerId)) {
            throw FavoriteBusinessExistsForUserException()
        }

        val fav = FavBusinessEntity(
            userId = userId,
            partnerId = favBusinessRequest.partnerId
        )
        return favBusinessRepository.save(fav)
    }

    override fun removeFavBusinesses(
        userId: Long,
        favBusinessesRequest: FavBusinessesRequest
    ) {
        favBusinessRepository.deleteByUserIdAndPartnerIdIn(userId, favBusinessesRequest.partnerIds)
    }

    override fun removeOneFavBusiness(
        userId: Long,
        favBusinessRequest: FavBusinessRequest
    ) {
        favBusinessRepository.deleteByUserIdAndPartnerId(userId, favBusinessRequest.partnerId)
    }

    override fun clearAllFavBusinesses(userId: Long) {
        favBusinessRepository.deleteAllByUserId(userId)
    }

}