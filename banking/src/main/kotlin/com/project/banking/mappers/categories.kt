package com.project.banking.mappers

import com.project.common.data.requests.categories.CategoryRequest
import com.project.banking.entities.CategoryEntity
import com.project.common.data.responses.categories.CategoryDto

fun CategoryRequest.toEntity() = CategoryEntity(name = name)

fun CategoryEntity.toDto(): CategoryDto = CategoryDto(
    id = id!!,
    name = name!!
)