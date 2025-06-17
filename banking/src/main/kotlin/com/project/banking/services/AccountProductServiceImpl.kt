package com.project.banking.services

import com.project.banking.entities.AccountEntity
import com.project.banking.entities.AccountProductEntity
import com.project.banking.repositories.AccountProductRepository
import com.project.common.data.requests.accountProducts.CreateAccountProductRequest
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.repository.findByIdOrNull
import org.springframework.web.multipart.MultipartFile

class AccountProductServiceImpl(
    private val accountProductRepository: AccountProductRepository,
    private val fileStorageService: FileStorageService,
    @Value("\${aws.endpoint}") val endpoint: String
): AccountProductService {
    override fun createNewAccountProduct(
        image: MultipartFile,
        newAccountProduct: CreateAccountProductRequest
    ): AccountProductEntity {

        val (publicBucket, imageUrl) = fileStorageService.uploadFile(image, true)

        val accountProduct = accountProductRepository.save(
            AccountProductEntity(
                name = newAccountProduct.name,
                image = "$endpoint/$publicBucket/$imageUrl",
                accountType = newAccountProduct.accountType,
                interestRate = newAccountProduct.interestRate,
                minBalanceRequired = newAccountProduct.minBalance,
                creditLimit = newAccountProduct.creditLimit,
                annualFee = newAccountProduct.annualFee,
                minSalary = newAccountProduct.minSalary
            )
        )

        return accountProduct
    }

    override fun getAllAccountProducts(): List<AccountProductEntity> {
        return accountProductRepository.findAll()
    }

    override fun getAccountProductById(id: Long): AccountProductEntity? {
        return accountProductRepository.findByIdOrNull(id)
    }

    override fun deleteAccountProductById(id: Long) {
        accountProductRepository.deleteById(id)
    }
}