package com.project.recommendation.entities

import jakarta.persistence.*
import java.math.BigDecimal

@Entity
@Table(name = "account_scores")
data class AccountScoreEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    var id: Long?,

    @Column(name = "account_id", nullable = false)
    var accountId: Long,

    @Column(name = "account_score_rating", nullable = false)
    var accountScoreRating: BigDecimal,

    @Column(name = "user_id", nullable = false)
    var userId: Long
)