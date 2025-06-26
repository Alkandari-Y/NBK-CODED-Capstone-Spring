package com.project.banking.listeners

import com.project.banking.events.KycCreatedEvent
import com.project.banking.providers.RecommendationServiceProvider
import com.project.banking.services.AccountProductService
import com.project.banking.services.AccountService
import com.project.banking.services.XpService
import com.project.common.enums.AccountType
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class KycEventListener(
    private val accountProductService: AccountProductService,
    private val accountService: AccountService,
    private val xpService: XpService,
    private val recommendationServiceProvider: RecommendationServiceProvider,
) {

    @EventListener
    fun handleKycCreated(event: KycCreatedEvent) {
        val salaryAccountProduct = accountProductService.findByAccountTypeAndName(AccountType.DEBIT, "Salary Account")
            ?: return
        val cashBackAccountProduct = accountProductService.findAllByAccountType(AccountType.CASHBACK).firstOrNull()
            ?: return

        val userId = event.kyc.userId!!
        accountService.createNewClientPackage(
            userId,
            listOf(salaryAccountProduct, cashBackAccountProduct)
        )
        xpService.userXpInit(userId)
        recommendationServiceProvider.initializeCategoryScores(userId)
        println("Created new client package for $userId, starting at 0 XP, and initialized category scores.")
    }
}