package com.project.common.data.responses.categories


// used for all categories endpoint
data class CategoryWithPerksDto(
    val id: Long,
    val name: String,
    val hasPerks: Boolean // filter by this field for user category favorites
)