package com.project.common.exceptions.notifications

class NotificationNotFoundException(
    message: String = "Notification not found"
) : RuntimeException(message) {
}