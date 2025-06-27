package com.project.common.exceptions

class NotificationNotFoundException(
    message: String = "Notification not found"
) : RuntimeException(message) {
}