package com.project.notification.entities

import jakarta.persistence.*

@Entity
@Table(name = "user_devices")
data class UserDeviceEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    var id: Long? = null,

    @Column(name = "firebase_token", nullable = false, unique = true, length = 255)
    var firebaseToken: String,

    @Column(name = "user_id", nullable = false)
    var userId: Long
)
