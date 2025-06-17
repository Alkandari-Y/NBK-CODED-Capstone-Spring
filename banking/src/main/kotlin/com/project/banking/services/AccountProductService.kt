package com.project.banking.services

import com.project.banking.entities.AccountProductEntity
import com.project.common.data.requests.accountProducts.CreateAccountProductRequest
import org.springframework.web.multipart.MultipartFile

interface AccountProductService {
    fun createNewAccountProduct(
        image: MultipartFile,
        newAccountProduct: CreateAccountProductRequest
    ): AccountProductEntity
    fun getAllAccountProducts() : List<AccountProductEntity>
    fun getAccountProductById(id : Long) : AccountProductEntity?
    fun deleteAccountProductById(id : Long)
}