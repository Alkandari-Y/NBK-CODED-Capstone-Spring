package com.project.banking.accounts

import com.project.banking.entities.AccountEntity
import com.project.banking.mappers.toBasicResponse
import com.project.banking.mappers.toEntity
import com.project.banking.services.AccountService
import com.project.banking.services.KYCService
import com.project.banking.services.TransactionService
import com.project.common.data.requests.accounts.AccountBalanceCheck
import com.project.common.data.requests.accounts.AccountCreateRequest
import com.project.common.data.requests.accounts.AccountResponse
import com.project.common.data.requests.accounts.TransferCreateRequest
import com.project.common.data.requests.accounts.UpdateAccountRequest
import com.project.common.data.requests.accounts.UserAccountsResponse
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
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/accounts")
class AccountsControllers(
    private val accountService: AccountService,
    private val transactionService: TransactionService,
    private val kycService: KYCService
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
            accountCreateRequestDto.toEntity(),
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

    @PutMapping(path=["/details/{accountNumber}"])
    fun updateAccount(
        @PathVariable accountNumber : String,
        @Valid @RequestBody accountUpdate: UpdateAccountRequest,
        @RequestAttribute("authUser") authUser: UserInfoDto,
    ): ResponseEntity<AccountResponse>{
        val updatedAccount = accountService.updateAccount(
            accountNumber = accountNumber,
            userId = authUser.userId,
            accountUpdate = accountUpdate,
        ).toBasicResponse()
        return ResponseEntity(updatedAccount, HttpStatus.OK)
    }

    @GetMapping(path=["/details/{accountNumber}"])
    fun getAccountDetails(
        @PathVariable accountNumber : String,
        @AuthenticationPrincipal user: RemoteUserPrincipal
    ): ResponseEntity<AccountResponse>{
        println("in controller $accountNumber")
        val account = accountService.getByAccountNumber(accountNumber)
            ?: throw AccountNotFoundException()

        val isOwner = account.ownerId == user.getUserId()
        val isAdmin = user.authorities.any { it.authority == "ROLE_ADMIN" }

        if (!isOwner && !isAdmin) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build()
        }

        if (account.active.not() && isAdmin.not()) {
            throw APIException(
                message = "Account is not active anymore",
                httpStatus = HttpStatus.BAD_REQUEST,
                code = ErrorCode.ACCOUNT_NOT_ACTIVE,
            )
        }

        return ResponseEntity(account.toBasicResponse(), HttpStatus.OK)
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/clients/{clientId}")
    fun getUserAccounts(@PathVariable("clientId") clientId: Long): UserAccountsResponse {
        val kyc = kycService.findKYCByUserId(clientId)
            ?: throw AccountNotFoundException("User not found")

        val accounts = accountService.getAllAccountsByUserId(clientId)
            .map {
                AccountBalanceCheck(
                    accountId = it.id!!,
                    accountNumber = it.accountNumber,
                    balance = it.balance,
                    accountType = it.accountType.name,
                )
            }

        return UserAccountsResponse(
            userId = kyc.userId!!,
            firstName = kyc.firstName,
            lastName = kyc.lastName,
            dateOfBirth = kyc.dateOfBirth,
            salary = kyc.salary,
            nationality = kyc.nationality,
            accounts = accounts
        )
    }

    @GetMapping("/clients")
    fun getAccountDetails(
        @RequestParam(required = false) accountId: Long?,
        @RequestParam(required = false) accountNumber: String?,
        @AuthenticationPrincipal user: RemoteUserPrincipal
    ): AccountResponse {
        val account = when {
            accountId != null -> accountService.getAccountById(accountId)
            accountNumber != null -> accountService.getByAccountNumber(accountNumber)
            else -> throw APIException("Either accountId or accountNumber must be provided")
        } ?: throw AccountNotFoundException()

        val isOwner = account.ownerId == user.getUserId()
        val isAdmin = user.authorities.any { it.authority == "ROLE_ADMIN" }

        if (!isOwner && !isAdmin) {
            throw APIException(
                "Unauthorized access to account",
                HttpStatus.UNAUTHORIZED,
                ErrorCode.UNAUTHORIZED
            )
        }
        return account.toBasicResponse()
    }
}