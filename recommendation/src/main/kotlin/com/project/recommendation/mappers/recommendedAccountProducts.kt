package com.project.recommendation.mappers

import com.project.common.data.requests.recommendations.RecommendedAccountProducts
import com.project.common.data.responses.accountProducts.AccountProductDto

fun AccountProductDto.toRecommendedAccountProduct(
    recommended: Boolean,
    isOwned: Boolean,
) = RecommendedAccountProducts(
    id = this.id,
    name = this.name,
    description = this.description,
    accountType = this.accountType,
    interestRate = this.interestRate,
    minBalanceRequired = this.minBalanceRequired,
    creditLimit = this.creditLimit,
    annualFee = this.annualFee,
    minSalary = this.minSalary,
    perks = this.perks,
    categoryIds = this.categoryIds,
    categoryNames = this.categoryNames,
    image = this.image,
    recommended = recommended,
    isOwned = isOwned,
)