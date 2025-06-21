package com.project.banking.config

import com.project.banking.entities.AccountEntity
import com.project.banking.entities.AccountProductEntity
import com.project.banking.entities.BusinessPartnerEntity
import com.project.banking.entities.CategoryEntity
import com.project.banking.repositories.AccountProductRepository
import com.project.banking.repositories.AccountRepository
import com.project.banking.repositories.BusinessPartnerRepository
import com.project.banking.repositories.CategoryRepository
import com.project.common.enums.AccountOwnerType
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
    private val businessPartnerRepository: BusinessPartnerRepository,
    private val accountRepository: AccountRepository
) {
    @EventListener(ApplicationReadyEvent::class)
    @Transactional
    fun seedInitialData() {
        println("Delaying data seeding for 5 seconds...")
        Thread.sleep(5000)
        println("Starting data seeding...")
        val categories = seedCategories()
        val accountProducts = seedAccountProducts()

        seedBusinessPartners(categories, accountProducts)
    }


fun seedCategories(): List<CategoryEntity> {
        if (categoryRepository.count() > 0L) {
            println("Categories already exist. Skipping seeding.")
            return categoryRepository.findAll()
        }
        val categories = listOf(
            "personal", "retail", "manufacturing", "healthcare", "financial services",
            "real estate", "technology", "hospitality", "education", "logistics",
            "construction", "agriculture", "automotive", "consulting", "wholesale", "energy"
        ).map { name -> CategoryEntity(name = name) }

        val categoryEntities = categoryRepository.saveAll(categories)
        println("Seeded default categories.")

        return categoryEntities
    }

    fun seedAccountProducts(): List<AccountProductEntity> {
        if (accountProductRepository.count() > 0L) {
            val accountProducts = accountProductRepository.findAll()
            println("Account products already exist. Skipping seeding.")
            return accountProducts
        }
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

        val accountProductEntities = accountProductRepository.saveAll(listOf(debitAccount, businessAccount, cashBackAccount, basicCreditCard, standardCreditCard, premiumCreditCard, ))
        println("Seeded default account products.")
        return accountProductEntities
    }

    fun seedBusinessPartners(
        categories: List<CategoryEntity>,
        accountProducts: List<AccountProductEntity>
    ) {
        if (businessPartnerRepository.count() > 0L) {
            println("Business partners already exist. Skipping seeding.")
            return
        }

        val businessAccountProduct = accountProducts.firstOrNull { it.name == "Business Account" }
            ?: throw IllegalStateException("Business Account product not found")

        val businesses = listOf(
            "TechNova", "GreenGrocers", "BuildSmart", "MediCare+", "LogiLink",
            "AutoMax", "EduSphere", "HealthWare", "FinEdge", "UrbanScape"
        )

        val logoBase = "http://localhost:9000/capstone-public/"

        val businessPartners = businesses.mapIndexed { i, name ->
            val category = categories[i % categories.size]

            val account = AccountEntity(
                balance = BigDecimal.valueOf((1000..10000).random().toDouble()),
                isActive = true,
                ownerId = 1L,
                ownerType = AccountOwnerType.BUSINESS,
                accountProduct = businessAccountProduct,
                accountType = AccountType.DEBIT
            )

            BusinessPartnerEntity(
                name = name,
                adminUser = 1L,
                account = account,
                logoUrl = "$logoBase${name.lowercase()}.png",
                category = category
            )
        }

        val accounts = businessPartners.map { it.account!! }
        accountRepository.saveAll(accounts)
        businessPartnerRepository.saveAll(businessPartners)

        println("Seeded ${businessPartners.size} business partners.")
    }
}
