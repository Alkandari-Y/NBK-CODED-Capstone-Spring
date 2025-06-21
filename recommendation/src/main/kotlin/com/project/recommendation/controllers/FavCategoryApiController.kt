package com.project.recommendation.controllers

import com.project.common.data.requests.categories.FavCategoriesRequest
import com.project.common.data.requests.categories.FavoriteCategoryRequest
import com.project.common.data.responses.authentication.UserInfoDto
import com.project.common.data.responses.categories.FavoriteCategoriesResponse
import com.project.recommendation.mappers.toFavoriteCategoryResponse
import com.project.recommendation.services.FavCategoriesService
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
@RequestMapping("/api/v1/fav/categories")
class FavCategoryApiController(
    private val favCategoriesService: FavCategoriesService
) {
    @GetMapping
    fun getAllFavoriteCategories(
        @RequestAttribute("authUser") authUser: UserInfoDto,
        ): ResponseEntity<FavoriteCategoriesResponse> {
        val favorites = favCategoriesService.findAllFavCategories(authUser.userId)
        return ResponseEntity.ok(favorites.toFavoriteCategoryResponse())
    }

    @PostMapping
    fun setAllFavoriteCategories(
        @RequestAttribute("authUser") authUser: UserInfoDto,
        @RequestBody request: FavCategoriesRequest
    ): ResponseEntity<FavoriteCategoriesResponse> {
        val updated = favCategoriesService.setAllFavCategories(request, authUser.userId)
        return ResponseEntity.ok(updated.toFavoriteCategoryResponse())
    }

    @PutMapping
    fun addFavoriteCategory(
        @RequestAttribute("authUser") authUser: UserInfoDto,
        @RequestBody request: FavoriteCategoryRequest
    ): ResponseEntity<Unit> {
        favCategoriesService.addOneFavCategory(request,authUser.userId)
        return ResponseEntity.ok().build()
    }

    @DeleteMapping
    fun removeFavoriteCategories(
        @RequestAttribute("authUser") authUser: UserInfoDto,
        @RequestBody request: FavCategoriesRequest
    ): ResponseEntity<Unit> {
        favCategoriesService.removeFavCategories(authUser.userId, request)
        return ResponseEntity.noContent().build()
    }

    @DeleteMapping("/remove")
    fun removeOneFavoriteCategory(
        @RequestAttribute("authUser") authUser: UserInfoDto,
        @RequestBody request: FavoriteCategoryRequest
    ): ResponseEntity<Unit> {
        favCategoriesService.removeOneFavCategory(authUser.userId, request)
        return ResponseEntity.noContent().build()
    }

    @DeleteMapping("/clear")
fun clearAllFavoriteCategories(
        @RequestAttribute("authUser") authUser: UserInfoDto,
    ): ResponseEntity<Unit> {
        favCategoriesService.clearAllFavCategories(authUser.userId)
        return ResponseEntity.noContent().build()
    }
}