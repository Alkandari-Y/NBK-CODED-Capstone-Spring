package com.project.recommendation.entities

import com.project.common.enums.RecommendationType
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "recommendations")
data class RecommendationEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = true)
    var id: Long? = null,

    @Column(name = "generic_id_ref", nullable = true)
    var genericIdRef: Long? = null,

    @Column(name = "user_id", nullable = false)
    var userId: Long? = null,

    @Column(name = "rec_type", nullable = false)
    var recType: RecommendationType? = null,

    @Column(name = "created_at", nullable = true)
    var createdAt: LocalDateTime = LocalDateTime.now(),
)