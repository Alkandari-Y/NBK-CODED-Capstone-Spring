package com.project.common.data.requests

data class AccountScoreNotification(
    val userId: Long,
    val accountProductName: String,
    val accountProductId: Long,
    val recommendationId: Long,
    val title: String,
    val message: String
)