package com.project.banking.controllers

import com.project.banking.permissions.hasAccountPermission
import com.project.banking.services.AccountService
import com.project.banking.services.TransactionService
import com.project.common.data.responses.transactions.TransactionDetails
import com.project.common.exceptions.accounts.AccountNotFoundException
import com.project.common.security.RemoteUserPrincipal
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/transactions")
class TransactionsAPIController(
    private val transactionService: TransactionService,
    private val accountService: AccountService,
){


    @GetMapping("/account")
    fun getAllTransactionsByAccount(
        @AuthenticationPrincipal user: RemoteUserPrincipal,
        @RequestParam(required = false) accountId: Long?,
        @RequestParam(required = false) accountNumber: String?
    ): ResponseEntity<List<TransactionDetails>> {
        if ((accountId == null && accountNumber == null) || (accountId != null && accountNumber != null)) {
            return ResponseEntity.badRequest().build()
        }

        val account = when {
            accountId != null -> accountService.getAccountById(accountId)
            accountNumber != null -> accountService.getByAccountNumber(accountNumber)
            else -> null
        } ?: throw AccountNotFoundException()

        hasAccountPermission(account, user)

        val transactions = transactionService.getTransactionsByAccount(accountId, accountNumber)
        return ResponseEntity(transactions, HttpStatus.OK)
    }

    // used by admin only
    @GetMapping("/clients/{clientId}")
    fun getTransactionsByClientId(
        @PathVariable("clientId") clientId: Long,
        @AuthenticationPrincipal user: RemoteUserPrincipal
    ): ResponseEntity<List<TransactionDetails>> {

        val isOwner = user.getUserId() == clientId
        val isAdmin = user.authorities.any { it.authority == "ROLE_ADMIN" }

        if (!isOwner && !isAdmin) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build()
        }

        return ResponseEntity(
            transactionService.getAllTransactionByUserId(clientId),
            HttpStatus.OK
        )
    }

    // used by rec service
    @GetMapping("/internal/{clientId}")
    fun getTransactionsByClientIdInternal(
        @PathVariable("clientId") clientId: Long
    ): ResponseEntity<List<TransactionDetails>> {
        return ResponseEntity(
            transactionService.getAllTransactionByUserId(clientId),
            HttpStatus.OK
        )
    }
}