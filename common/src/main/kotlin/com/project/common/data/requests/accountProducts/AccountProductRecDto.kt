package com.project.common.data.requests.accountProducts

data class AccountProductRecDto (
    val userId: Long,
    val totalNumTransactions: Int,
    val totalNumValidPerkPurchases: Int,
    val accountScore: Double,
    val currentAccountProductId: Long,
    val currentAccountId: Long,
    val listOfOwnedUniqueAccountProductIds: List<Long>
)