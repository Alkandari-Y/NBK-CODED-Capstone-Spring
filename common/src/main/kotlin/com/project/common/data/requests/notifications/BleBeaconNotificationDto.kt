package com.project.common.data.requests.notifications

import com.project.common.enums.NotificationTriggerType

data class BleBeaconNotificationDto(
    var userId: Long,
    var message: String,
    var partnerId: Long? = null,
    var recommendationId: Long? = null,
    var promotionId: Long? = null,
    var triggerType: NotificationTriggerType? = null,
    var accountProductId: Long? = null
)