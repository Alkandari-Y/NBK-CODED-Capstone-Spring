package com.project.banking.accounts

import com.project.banking.entities.AccountEntity
import com.project.banking.mappers.toDto
import com.project.banking.services.AccountService
import com.project.banking.services.TransactionService
import com.project.common.data.requests.accounts.AccountCreateRequest
import com.project.common.data.requests.accounts.TransferCreateRequest
import com.project.common.data.responses.accounts.AccountDto
import com.project.common.data.responses.accounts.TransactionResponse
import com.project.common.data.responses.authentication.UserInfoDto
import com.project.common.exceptions.APIException
import com.project.common.enums.ErrorCode
import com.project.common.exceptions.accounts.AccountNotFoundException
import com.project.common.security.RemoteUserPrincipal
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/accounts")
class AccountsControllers(
    private val accountService: AccountService,
    private val transactionService: TransactionService,
) {
    @GetMapping
    fun getAllAccounts(
        @AuthenticationPrincipal user: RemoteUserPrincipal
    ): List<AccountDto> {
        return accountService.getActiveAccountsByUserId(user.getUserId())
    }

    @PostMapping
    fun createAccount(
        @Valid @RequestBody accountCreateRequestDto : AccountCreateRequest,
        @RequestAttribute("authUser") authUser: UserInfoDto,
    ) : ResponseEntity<AccountEntity>
    {
        val account = accountService.createClientAccount(
            accountCreateRequestDto,
            authUser,
        )
        return ResponseEntity(account, HttpStatus.CREATED)
    }

    @PostMapping(path=["/transfer"])
    fun transfer(
        @Valid @RequestBody transferCreateRequestDto: TransferCreateRequest,
        @RequestAttribute("authUser") authUser: UserInfoDto,
        ): ResponseEntity<TransactionResponse> {
            val result = transactionService.transfer(
                transferCreateRequestDto,
                userIdMakingTransfer = authUser.userId,
            )
            return ResponseEntity(
                result,
                HttpStatus.OK
            )
    }

    @DeleteMapping(path=["/close/{accountNumber}"])
    fun closeAccount(
        @PathVariable accountNumber : String,
        @RequestAttribute("authUser") authUser: UserInfoDto,
        ): ResponseEntity<Unit> {
        accountService.closeAccount(accountNumber, authUser)
        return ResponseEntity(HttpStatus.OK)
    }

    @GetMapping("/details")
    fun getAccountDetails(
        @RequestParam(required = false) accountId: Long?,
        @RequestParam(required = false) accountNumber: String?,
        @AuthenticationPrincipal user: RemoteUserPrincipal
    ): ResponseEntity<AccountDto> {
        if (accountId == null && accountNumber == null) {
            throw APIException(
                message = "You must provide either accountId or accountNumber",
                httpStatus = HttpStatus.BAD_REQUEST,
                code = ErrorCode.INVALID_INPUT
            )
        }

        val account = when {
            accountId != null -> accountService.getAccountById(accountId)
            accountNumber != null -> accountService.getByAccountNumber(accountNumber)
            else -> null
        } ?: throw AccountNotFoundException()

        val isOwner = account.ownerId == user.getUserId()
        val isAdmin = user.authorities.any { it.authority == "ROLE_ADMIN" }

        if (!isOwner && !isAdmin) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build()
        }

        if (account.isActive.not() && isAdmin.not()) {
            throw APIException(
                message = "Account is not active anymore",
                httpStatus = HttpStatus.BAD_REQUEST,
                code = ErrorCode.ACCOUNT_NOT_ACTIVE,
            )
        }

        return ResponseEntity(account.toDto(), HttpStatus.OK)
    }
}