package com.project.banking.services

import com.project.banking.entities.AccountProductEntity
import com.project.banking.mappers.toEntity
import com.project.banking.projections.AccountProductView
import com.project.banking.repositories.AccountProductRepository
import com.project.common.data.requests.accountProducts.CreateAccountProductRequest
import com.project.common.enums.AccountType
import com.project.common.exceptions.accountProducts.AccountProductNotFoundException
import org.springframework.beans.factory.annotation.Value
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
@Service
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
            newAccountProduct.toEntity(
                imageUrl = "$endpoint/$publicBucket/$imageUrl"
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
        val accountProduct = accountProductRepository.findByIdOrNull(id)
            ?: throw AccountProductNotFoundException()
        accountProductRepository.deleteById(accountProduct.id!!)
    }

    override fun findAllByAccountType(accountType: AccountType): List<AccountProductEntity> {
        return accountProductRepository.findByAccountType(accountType)
    }

    override fun findByName(name: String): AccountProductEntity? {
        return accountProductRepository.findByName(name)
    }

    override fun findByAccountTypeAndName(
        accountType: AccountType,
        name: String
    ): AccountProductEntity? {
        return accountProductRepository.findByAccountTypeAndName(accountType, name)
    }
}