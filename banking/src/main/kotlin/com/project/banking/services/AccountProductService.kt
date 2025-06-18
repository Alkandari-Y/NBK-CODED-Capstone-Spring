package com.project.banking.services

import com.project.banking.entities.AccountProductEntity
import com.project.common.data.requests.accountProducts.CreateAccountProductRequest
import com.project.common.enums.AccountType
import org.springframework.web.multipart.MultipartFile

interface AccountProductService {
    fun createNewAccountProduct(
        image: MultipartFile,
        newAccountProduct: CreateAccountProductRequest
    ): AccountProductEntity
    fun getAllAccountProducts() : List<AccountProductEntity>
    fun getAccountProductById(id : Long) : AccountProductEntity?
    fun deleteAccountProductById(id : Long)

    fun findAllByAccountType(accountType: AccountType) : List<AccountProductEntity>
    fun findByName(name: String) : AccountProductEntity?
    fun findByAccountTypeAndName(accountType: AccountType, name: String) : AccountProductEntity?

}