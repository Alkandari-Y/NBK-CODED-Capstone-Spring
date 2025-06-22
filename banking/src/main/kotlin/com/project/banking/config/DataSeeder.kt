package com.project.banking.config

import com.project.banking.entities.AccountEntity
import com.project.banking.entities.AccountProductEntity
import com.project.banking.entities.BusinessPartnerEntity
import com.project.banking.entities.CategoryEntity
import com.project.banking.entities.PerkEntity
import com.project.banking.entities.XpTierEntity
import com.project.banking.repositories.AccountProductRepository
import com.project.banking.repositories.AccountRepository
import com.project.banking.repositories.BusinessPartnerRepository
import com.project.banking.repositories.CategoryRepository
import com.project.banking.repositories.PerkRepository
import com.project.banking.repositories.XpTierRepository
import com.project.common.enums.AccountOwnerType
import com.project.common.enums.AccountType
import com.project.common.enums.RewardType
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
    private val accountRepository: AccountRepository,
    private val xpTierRepository: XpTierRepository,
    private val perkRepository: PerkRepository
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
        seedXpTiers()
        seedPerks(accountProducts, categories)
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

        val accountProductEntities = accountProductRepository.saveAll(
            listOf(
                debitAccount,
                businessAccount,
                cashBackAccount,
                basicCreditCard,
                standardCreditCard,
                premiumCreditCard,
            )
        )
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

    fun seedXpTiers() {
        if (xpTierRepository.count() > 0L) {
            println("XP Tiers already exist. Skipping seeding.")
            return
        }

        val tiers = listOf(
            XpTierEntity(
                name = "Silver",
                minXp = 0,
                maxXp = 4999,
                xpPerkMultiplier = BigDecimal(1.0),
                xpPerNotification = 10,
                xpPerPromotion = 20,
                perkAmountPercentage = 50
            ),
            XpTierEntity(
                name = "Gold",
                minXp = 5000,
                maxXp = 14999,
                xpPerkMultiplier = BigDecimal(1.2),
                xpPerNotification = 15,
                xpPerPromotion = 25,
                perkAmountPercentage = 75
            ),
            XpTierEntity(
                name = "Platinum",
                minXp = 15000,
                maxXp = 999999,
                xpPerkMultiplier = BigDecimal(1.5),
                xpPerNotification = 20,
                xpPerPromotion = 30,
                perkAmountPercentage = 100
            )
        )
        xpTierRepository.saveAll(tiers)
        println("Seeded XP Tiers: ${tiers.joinToString { it.name.toString() }}.")
    }


    fun seedPerks(accountProducts: List<AccountProductEntity>, categories: List<CategoryEntity>) {
        if (perkRepository.count() > 0L) {
            println("Perks already exist. Skipping seeding.")
            return
        }

        val perks = listOf(
            // Basic Credit
            PerkEntity(
                type = RewardType.CASHBACK,
                minPayment = BigDecimal("20.00"),
                rewardsXp = 10,
                perkAmount = BigDecimal("2.00"),
                isTierBased = false,
                accountProduct = accountProducts.first { it.name == "Basic Credit" },
                categories = categories.filter { it.name in listOf("education", "retail") }.toMutableList()
            ),
            PerkEntity(
                type = RewardType.DISCOUNT,
                minPayment = BigDecimal("35.00"),
                rewardsXp = 12,
                perkAmount = BigDecimal("3.50"),
                isTierBased = true,
                accountProduct = accountProducts.first { it.name == "Basic Credit" },
                categories = categories.filter { it.name in listOf("personal", "technology") }.toMutableList()
            ),

            // Standard Credit
            PerkEntity(
                type = RewardType.CASHBACK,
                minPayment = BigDecimal("30.00"),
                rewardsXp = 15,
                perkAmount = BigDecimal("4.00"),
                isTierBased = false,
                accountProduct = accountProducts.first { it.name == "Standard Credit" },
                categories = categories.filter { it.name in listOf("logistics", "healthcare") }.toMutableList()
            ),
            PerkEntity(
                type = RewardType.DISCOUNT,
                minPayment = BigDecimal("60.00"),
                rewardsXp = 25,
                perkAmount = BigDecimal("6.00"),
                isTierBased = true,
                accountProduct = accountProducts.first { it.name == "Standard Credit" },
                categories = categories.filter { it.name in listOf("wholesale", "consulting") }.toMutableList()
            ),

            // Infinity
            PerkEntity(
                type = RewardType.CASHBACK,
                minPayment = BigDecimal("100.00"),
                rewardsXp = 30,
                perkAmount = BigDecimal("10.00"),
                isTierBased = true,
                accountProduct = accountProducts.first { it.name == "Infinity" },
                categories = categories.filter { it.name in listOf("hospitality", "real estate") }.toMutableList()
            ),
            PerkEntity(
                type = RewardType.DISCOUNT,
                minPayment = BigDecimal("80.00"),
                rewardsXp = 28,
                perkAmount = BigDecimal("12.00"),
                isTierBased = true,
                accountProduct = accountProducts.first { it.name == "Infinity" },
                categories = categories.filter { it.name in listOf("automotive", "energy") }.toMutableList()
            )
        )

        perkRepository.saveAll(perks)
        println("Seeded sample perks.")
    }
}
