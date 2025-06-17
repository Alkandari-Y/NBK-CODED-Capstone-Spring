package com.project.banking.entities

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "business_partners")
data class BusinessPartnerEntity (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
     var id: Long? = null,

    @Column(name = "admin_user", nullable = false)
     var adminUser: Long? = null,

    @Column(name = "account_id", nullable = false)
     var accountId: Long? = null,

    @Column(name = "logo_url", nullable = false)
     var logoUrl: String? = null,

    @Column(name = "category_id", nullable = false)
     var categoryId: Long? = null,
)