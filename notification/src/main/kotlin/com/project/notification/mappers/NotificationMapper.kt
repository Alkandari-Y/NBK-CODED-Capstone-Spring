package com.project.notification.mappers

import com.project.notification.entities.NotificationEntity
import com.project.common.data.responses.notifications.NotificationResponseDto

fun NotificationEntity.toResponseDto(): NotificationResponseDto {
    return NotificationResponseDto(
        id = this.id!!,
        userId = this.userId,
        message = this.message,
        deliveryType = this.deliveryType,
        createdAt = this.createdAt,
        delivered = this.delivered,
        partnerId = this.partnerId,
        eventId = this.eventId,
        recommendationId = this.recommendationId,
        promotionId = this.promotionId,
        triggerType = this.triggerType
    )
}
