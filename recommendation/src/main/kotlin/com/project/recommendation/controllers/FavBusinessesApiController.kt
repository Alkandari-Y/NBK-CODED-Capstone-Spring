package com.project.recommendation.controllers

import com.project.common.data.requests.businessPartners.FavBusinessRequest
import com.project.common.data.requests.businessPartners.FavBusinessesRequest
import com.project.common.data.requests.categories.FavCategoriesRequest
import com.project.common.data.requests.categories.FavoriteCategoryRequest
import com.project.common.data.responses.authentication.UserInfoDto
import com.project.common.data.responses.businessPartners.FavoriteBusinessDto
import com.project.common.data.responses.businessPartners.FavoriteBusinessesResponse
import com.project.recommendation.mappers.toFavoriteBusinessResponse
import com.project.recommendation.mappers.toFavoriteCategoryResponse
import com.project.recommendation.services.FavBusinessService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestAttribute
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/v1/fav/businesses")
class FavBusinessesApiController(
    private val favBusinessService: FavBusinessService,
) {

    @GetMapping
    fun getAllFavoriteBusinesses(
        @RequestAttribute("authUser") authUser: UserInfoDto,
    ): ResponseEntity<FavoriteBusinessesResponse> {
        val favorites = favBusinessService.findAllFavBusinesses(authUser.userId)
        return ResponseEntity.ok(favorites.toFavoriteBusinessResponse())
    }

    @PostMapping
    fun setAllFavoriteBusinesses(
        @RequestAttribute("authUser") authUser: UserInfoDto,
        @RequestBody request: FavBusinessesRequest
    ): ResponseEntity<FavoriteBusinessesResponse> {
        val updated = favBusinessService.setAllFavBusinesses(request, authUser.userId)
        return ResponseEntity.ok(updated.toFavoriteBusinessResponse())
    }

    @PutMapping
    fun addFavoriteCategory(
        @RequestAttribute("authUser") authUser: UserInfoDto,
        @RequestBody request: FavBusinessRequest
    ): ResponseEntity<Unit> {
        favBusinessService.addOneFavBusiness(request,authUser.userId)
        return ResponseEntity.ok().build()
    }

    @DeleteMapping
    fun removeFavoriteCategories(
        @RequestAttribute("authUser") authUser: UserInfoDto,
        @RequestBody request: FavBusinessesRequest
    ): ResponseEntity<Unit> {
        favBusinessService.removeFavBusinesses(authUser.userId, request)
        return ResponseEntity.noContent().build()
    }

    @DeleteMapping("/remove")
    fun removeOneFavoriteCategory(
        @RequestAttribute("authUser") authUser: UserInfoDto,
        @RequestBody request: FavBusinessRequest
    ): ResponseEntity<Unit> {
        favBusinessService.removeOneFavBusiness(authUser.userId, request)
        return ResponseEntity.noContent().build()
    }

    @DeleteMapping("/clear")
    fun clearAllFavoriteBusinesses(
        @RequestAttribute("authUser") authUser: UserInfoDto,
    ): ResponseEntity<Unit> {
        favBusinessService.clearAllFavBusinesses(authUser.userId)
        return ResponseEntity.noContent().build()
    }
}