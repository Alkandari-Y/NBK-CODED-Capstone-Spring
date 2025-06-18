package com.project.banking.config

import com.project.banking.entities.AccountProductEntity
import com.project.banking.entities.CategoryEntity
import com.project.banking.repositories.AccountProductRepository
import com.project.banking.repositories.CategoryRepository
import com.project.common.enums.AccountType
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal

@Component
class DataSeeder(
    private val categoryRepository: CategoryRepository,
    private val accountProductRepository: AccountProductRepository,
) {
    @EventListener(ApplicationReadyEvent::class)
    @Transactional
    fun seedInitialData() {
        if (categoryRepository.count() == 0L) {
            seedCategories()
        } else {
            println("Categories already exist. Skipping seeding.")
        }

        if (accountProductRepository.count() == 0L) {
            seedAccountProducts()
        } else {
            println("Account products already exist. Skipping seeding.")
        }

    }


    fun seedCategories() {
        val categories = listOf(
            "personal", "retail", "manufacturing", "healthcare", "financial services",
            "real estate", "technology", "hospitality", "education", "logistics",
            "construction", "agriculture", "automotive", "consulting", "wholesale", "energy"
        ).map { name -> CategoryEntity(name = name) }

        categoryRepository.saveAll(categories)
        println("Seeded default categories.")
    }

    fun seedAccountProducts() {
            // 1. Debit Card – No Credit, No Fees
            val debitAccount = AccountProductEntity(
                name = "Salary Account",
                accountType = AccountType.DEBIT,
                interestRate = BigDecimal("0.000"),
                minBalanceRequired = BigDecimal("0.000"),
                creditLimit = BigDecimal("0.000"),
                annualFee = BigDecimal("0.000"),
                minSalary = BigDecimal("0.000"),
                image = "http://localhost:9000/capstone-public/fd4ac1cd-c88f-446a-846b-f23ecea8fe63"
            )

            // 2. Business debit Card – No Credit, No Fees
            val businessAccount = AccountProductEntity(
                name = "Business Account",
                accountType = AccountType.DEBIT,
                interestRate = BigDecimal("0.000"),
                minBalanceRequired = BigDecimal("0.000"),
                creditLimit = BigDecimal("0.000"),
                annualFee = BigDecimal("0.000"),
                minSalary = BigDecimal("0.000"),
                image = "http://localhost:9000/capstone-public/fd4ac1cd-c88f-446a-846b-f23ecea8fe63"
            )

            // 3. Debit Card – No Credit, No Fees
            val cashBackAccount = AccountProductEntity(
                name = "Cashback Wallet",
                accountType = AccountType.CASHBACK,
                interestRate = BigDecimal("0.000"),
                minBalanceRequired = BigDecimal("0.000"),
                creditLimit = BigDecimal("0.000"),
                annualFee = BigDecimal("0.000"),
                minSalary = BigDecimal("0.000"),
                image = "http://localhost:9000/capstone-public/fd4ac1cd-c88f-446a-846b-f23ecea8fe63"
            )

            // 4. Basic Credit Card – For Low-Income
            val basicCreditCard = AccountProductEntity(
                name = "Basic Credit",
                accountType = AccountType.CREDIT,
                interestRate = BigDecimal("3.000"),
                minBalanceRequired = BigDecimal("0.000"),
                creditLimit = BigDecimal("500.000"),
                annualFee = BigDecimal("25.000"),
                minSalary = BigDecimal("400.000"),
                image = "http://localhost:9000/capstone-public/fd4ac1cd-c88f-446a-846b-f23ecea8fe63"
            )

            // 5. Standard Credit Card – For Middle-Income
            val standardCreditCard = AccountProductEntity(
                name = "Standard Credit",
                accountType = AccountType.CREDIT,
                interestRate = BigDecimal("2.500"),
                minBalanceRequired = BigDecimal("0.000"),
                creditLimit = BigDecimal("2000.000"),
                annualFee = BigDecimal("50.000"),
                minSalary = BigDecimal("800.000"),
                image = "http://localhost:9000/capstone-public/fd4ac1cd-c88f-446a-846b-f23ecea8fe63"
            )

            // 6. Premium Credit Card – For High-Income
            val premiumCreditCard = AccountProductEntity(
                name = "Infinity",
                accountType = AccountType.CREDIT,
                interestRate = BigDecimal("2.000"),
                minBalanceRequired = BigDecimal("0.000"),
                creditLimit = BigDecimal("4000.000"),
                annualFee = BigDecimal("100.000"),
                minSalary = BigDecimal("1000.000"),
                image = "http://localhost:9000/capstone-public/fd4ac1cd-c88f-446a-846b-f23ecea8fe63"
            )

        accountProductRepository.saveAll(listOf(debitAccount, cashBackAccount, basicCreditCard, standardCreditCard, premiumCreditCard, ))
        println("Seeded default account products.")

    }
}
