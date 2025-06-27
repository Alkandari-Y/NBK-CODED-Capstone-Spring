package com.project.banking.controllers

import com.project.banking.entities.AccountEntity
import com.project.banking.mappers.toDto
import com.project.banking.services.AccountService
import com.project.banking.services.TransactionService
import com.project.common.data.requests.accounts.AccountCreateRequest
import com.project.common.data.requests.accounts.PaymentCreateRequest
import com.project.common.data.requests.accounts.TransferCreateRequest
import com.project.common.data.requests.ble.BleStoreLocationRecommendationDataRequest
import com.project.common.data.responses.accounts.UniqueUserProductsAndAllProducts
import com.project.common.data.responses.accounts.AccountDto
import com.project.common.data.responses.authentication.UserInfoDto
import com.project.common.data.responses.ble.BleUserRecommendationInput
import com.project.common.data.responses.transactions.PaymentDetails
import com.project.common.data.responses.transactions.TransactionDetails
import com.project.common.enums.ErrorCode
import com.project.common.exceptions.APIException
import com.project.common.exceptions.accounts.AccountNotFoundException
import com.project.common.security.RemoteUserPrincipal
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestAttribute
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/accounts")
class AccountApiController(
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

    @PostMapping("/onboarding/create")
    fun createOnBoardingAccount(
        @Valid @RequestBody accountCreateRequestDto : AccountCreateRequest,
        @RequestAttribute("authUser") authUser: UserInfoDto,
    ) : ResponseEntity<AccountEntity>
    {
        val account = accountService.onBoardingCreateAccount(
            accountCreateRequestDto,
            authUser,
        )
        return ResponseEntity(account, HttpStatus.CREATED)
    }

    @PostMapping(path=["/transfer"])
    fun transfer(
        @Valid @RequestBody transferCreateRequestDto: TransferCreateRequest,
        @RequestAttribute("authUser") authUser: UserInfoDto,
    ): ResponseEntity<TransactionDetails> {
        val result = transactionService.transfer(
            transferCreateRequestDto,
            userIdMakingTransfer = authUser.userId,
        )
        return ResponseEntity(
            result,
            HttpStatus.OK
        )
    }

    @PostMapping("/purchase")
    fun purchase(
        @Valid @RequestBody purchaseRequest: PaymentCreateRequest,
        @AuthenticationPrincipal user: RemoteUserPrincipal
    ): ResponseEntity<PaymentDetails> {
        val body = transactionService.purchase(user.getUserId(), purchaseRequest)
        return ResponseEntity(body, HttpStatus.OK)
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

    // used for ble recommendation fetching
    @PostMapping("/clients/products")
    fun getClientUniqueProductsAndAccountProducts(
        @Valid @RequestBody request: BleStoreLocationRecommendationDataRequest
    ): ResponseEntity<BleUserRecommendationInput> {
        return ResponseEntity(
            accountService.getClientUniqueProductsAndAccountProducts(request),
            HttpStatus.OK
        )
    }
}