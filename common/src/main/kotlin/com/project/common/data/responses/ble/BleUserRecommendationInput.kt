package com.project.common.data.responses.ble

import com.project.common.data.responses.accountProducts.AccountProductSummaryDto
import com.project.common.data.responses.accounts.UniqueUserProductsAndAllProducts
import com.project.common.data.responses.businessPartners.BusinessPartnerSummaryDto

data class BleUserRecommendationInput(
    val userData: UniqueUserProductsAndAllProducts,
    val allProducts: List<AccountProductSummaryDto>,
    val relatedPartnerSummary: BusinessPartnerSummaryDto
)