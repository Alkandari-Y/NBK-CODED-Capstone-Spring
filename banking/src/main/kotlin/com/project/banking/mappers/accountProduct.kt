package com.project.banking.mappers

import com.project.banking.entities.AccountProductEntity
import com.project.common.data.requests.accountProducts.CreateAccountProductRequest

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