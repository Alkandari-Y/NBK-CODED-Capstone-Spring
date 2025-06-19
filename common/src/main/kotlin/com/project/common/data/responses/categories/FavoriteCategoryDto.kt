package com.project.common.data.responses.categories

import java.time.LocalDateTime

data class FavoriteCategoryDto(
    var id: Long,
    var categoryId: Long,
    var createAt: LocalDateTime,
)
