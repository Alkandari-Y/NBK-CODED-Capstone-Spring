package com.project.notification.entities

import com.project.common.enums.NotificationDeliveryType
import com.project.common.enums.RewardType
import jakarta.persistence.*

@Entity
@Table(name = "notification_settings")
data class NotificationSettingEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    var id: Long? = null,

    @Column(name = "delivery_type", nullable = false)
    var deliveryType: NotificationDeliveryType,

    @Column(name = "user_id", nullable = false)
    var userId: Long,

    @Column(name = "reward_type", nullable = false)
    var rewardType: RewardType,

    @Column(name = "active", nullable = false)
    var active: Boolean
)
