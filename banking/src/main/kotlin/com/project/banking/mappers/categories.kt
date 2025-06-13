package com.project.banking.mappers

import com.project.common.data.requests.categories.CategoryRequest
import com.project.banking.entities.CategoryEntity

fun CategoryRequest.toEntity() = CategoryEntity(name = name)
