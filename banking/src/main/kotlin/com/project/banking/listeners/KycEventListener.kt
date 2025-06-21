package com.project.banking.listeners

import com.project.banking.entities.AccountProductEntity
import com.project.banking.events.KycCreatedEvent
import com.project.banking.services.AccountProductService
import com.project.banking.services.AccountService
import com.project.common.enums.AccountType
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class KycEventListener(
    private val accountProductService: AccountProductService,
    private val accountService: AccountService,
) {

    @EventListener
    fun handleKycCreated(event: KycCreatedEvent) {
        val salaryAccountProduct = accountProductService.findByAccountTypeAndName(AccountType.DEBIT, "Salary Account")
            ?: return
        val cashBackAccountProduct = accountProductService.findAllByAccountType(AccountType.CASHBACK).firstOrNull()
            ?: return

        accountService.createNewClientPackage(
            event.kyc.userId!!,
            listOf(salaryAccountProduct, cashBackAccountProduct)
        )
        println("Created new client package for ${event.kyc.userId}")
    }
}