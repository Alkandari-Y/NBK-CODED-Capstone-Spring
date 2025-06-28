package com.project.banking.mappers

import com.project.banking.entities.AccountEntity
import com.project.banking.entities.BusinessPartnerEntity
import com.project.banking.entities.CategoryEntity
import com.project.common.data.requests.businessPartners.CreateBusinessPartnerRequest
import com.project.common.data.responses.businessPartners.BusinessPartnerDto
import com.project.common.data.responses.businessPartners.BusinessPartnerSummaryDto
import com.project.common.data.responses.categories.CategoryDto

fun CreateBusinessPartnerRequest.toEntity(
    businessAccount: AccountEntity,
    category: CategoryEntity,
    userId: Long
): BusinessPartnerEntity {
    return BusinessPartnerEntity(
        name = this.name,
        logoUrl = this.logoUrl,
        account = businessAccount,
        category = category,
        adminUser = userId,
    )
}

fun BusinessPartnerEntity.toDto() = BusinessPartnerDto(
    id = this.id!!,
    name = this.name,
    logoUrl = this.logoUrl!!,
    category = CategoryDto(
        id = category?.id!!,
        name = category?.name!!
    ),
)

fun BusinessPartnerEntity.toSummaryDto() = BusinessPartnerSummaryDto(
    id = this.id!!,
    name = this.name,
    logoUrl = this.logoUrl!!,
    categoryId = this.category?.id!!
)

fun List<BusinessPartnerEntity>.toSummaryDto() = this.map { it.toSummaryDto() }