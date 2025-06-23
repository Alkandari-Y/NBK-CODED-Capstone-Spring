package com.project.banking.mappers

import com.project.banking.entities.AccountProductEntity
import com.project.common.data.requests.accountProducts.CreateAccountProductRequest
import com.project.common.data.responses.accountProducts.AccountProductDto

fun CreateAccountProductRequest.toEntity(imageUrl: String): AccountProductEntity {
    return AccountProductEntity(
        name = name,
        accountType = accountType,
        interestRate = interestRate?.setScale(3) ?: throw IllegalArgumentException("invalid interest rate") ,
        minBalanceRequired = minBalance?.setScale(3) ?: throw IllegalArgumentException("invalid min balance") ,
        creditLimit = creditLimit?.setScale(3) ?: throw IllegalArgumentException("invalid creditLimit") ,
        annualFee = annualFee?.setScale(3) ?: throw IllegalArgumentException("invalid annualFee") ,
        minSalary = minSalary?.setScale(3) ?: throw IllegalArgumentException("invalid minSalary") ,
        image = imageUrl,
    )
}

fun AccountProductEntity.toDto(): AccountProductDto {
    val affectedCategories = this.perks.flatMap { it.categories }
    val perksCategoryIds = affectedCategories.map { it.id!! }.toSet()
    val perksCategoryNames = affectedCategories.map { it.name!! }.toSet()
    return AccountProductDto(
        id = this.id!!,
        name = this.name,
        description = description,
        accountType = this.accountType.toString(),
        interestRate = this.interestRate,
        minBalanceRequired = this.minBalanceRequired,
        creditLimit = this.creditLimit,
        annualFee = this.annualFee,
        minSalary = this.minSalary,
        image = this.image,
        perks = this.perks.map {
            it.toDto()
        },
        categoryIds = perksCategoryIds,
        categoryNames = perksCategoryNames
    )
}

fun List<AccountProductEntity>.toDto(): List<AccountProductDto> {
    return this.map { it.toDto() }
}