package com.project.notification.entities

import com.project.common.enums.NotificationDeliveryType
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "notifications")
data class NotificationEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    var id: Long? = null,

    @Column(name = "user_id", nullable = false)
    var userId: Long,

    @Column(name = "message", nullable = false, length = 255)
    var message: String,

    @Column(name = "delivery_type", nullable = false)
    var deliveryType: NotificationDeliveryType,

    @Column(name = "created_at", nullable = false)
    var createdAt: LocalDateTime,

    @Column(name = "delivered", nullable = false)
    var delivered: Boolean,

    @Column(name = "partner_id")
    var partnerId: Long? = null,

    @Column(name = "event_id")
    var eventId: Long? = null,

    @Column(name = "recommendation_id")
    var recommendationId: Long? = null,

    @Column(name = "promotion_id")
    var promotionId: Long? = null,

    @Column(name = "trigger_type")
    var triggerType: Int? = null
)
