package com.project.common.data.requests.notifications

import com.project.common.data.requests.geofencing.GeofenceEventRequest
import com.project.common.enums.NotificationTriggerType

data class NotificationDto(
    var userId: Long,
    var message: String,
    var partnerId: Long? = null,
    var eventId: Long? = null,
    var recommendationId: Long? = null,
    var promotionId: Long? = null,
    var triggerType: NotificationTriggerType? = null,
    var geofenceEventRequest: GeofenceEventRequest
)
