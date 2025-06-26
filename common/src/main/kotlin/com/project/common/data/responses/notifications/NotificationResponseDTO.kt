package com.project.common.data.responses.notifications

import com.project.common.enums.NotificationDeliveryType
import com.project.common.enums.NotificationTriggerType
import java.time.LocalDateTime

data class NotificationResponseDto(
    val id: Long,
    val userId: Long,
    val message: String,
    val deliveryType: NotificationDeliveryType,
    val createdAt: LocalDateTime,
    val delivered: Boolean,
    val partnerId: Long? = null,
    val eventId: Long? = null,
    val recommendationId: Long? = null,
    val promotionId: Long? = null,
    val triggerType: NotificationTriggerType? = null
)
