package com.project.banking.services

import com.project.banking.entities.AccountEntity
import com.project.banking.entities.AccountProductEntity
import com.project.common.data.requests.accounts.AccountCreateRequest
import com.project.common.data.requests.ble.BleStoreLocationRecommendationDataRequest
import com.project.common.data.responses.accounts.UniqueUserProductsAndAllProducts
import com.project.common.data.responses.accounts.AccountDto
import com.project.common.data.responses.accounts.AccountWithProductResponse
import com.project.common.data.responses.authentication.UserInfoDto
import com.project.common.data.responses.ble.BleUserRecommendationInput

interface AccountService {
    fun getActiveAccountsByUserId(userId: Long): List<AccountDto>
    fun createClientAccount(accountRequest: AccountCreateRequest, userInfoDto: UserInfoDto): AccountEntity
    fun onBoardingCreateAccount(accountRequest: AccountCreateRequest, userInfoDto: UserInfoDto): AccountEntity
    fun closeAccount(accountNumber: String, user: UserInfoDto): Unit
    fun getAccountById(accountId: Long): AccountEntity?
    fun getByAccountNumber(accountNumber: String): AccountEntity?
    fun getAllAccountsByUserId(userId: Long): List<AccountDto>
    fun createNewClientPackage(userId: Long, accountProducts: List<AccountProductEntity>)
    fun getClientUniqueProductsAndAccountProducts(request: BleStoreLocationRecommendationDataRequest): BleUserRecommendationInput
    fun getAllAccountsInternal(userId: Long): List<AccountWithProductResponse>
}