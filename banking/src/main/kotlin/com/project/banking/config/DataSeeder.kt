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
    private val perkRepository: PerkRepository,
    private val xpTierRepository: XpTierRepository,
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
        seedPerks(accountProducts, categories)
        seedTiers()
    }


    fun seedCategories(): List<CategoryEntity> {
        val existing = categoryRepository.findAll().associateBy { it.name }

        val categoriesToSave = listOf(
            "retail", "travel", "dining", "fashion", "technology", "hospitality", "education", "entertainment",
            "personal care", "wholesale", "manufacturing", "healthcare", "financial services", "real estate",
            "logistics", "construction", "agriculture", "automotive", "personal", "consulting", "energy", "cashback",
        ).map { name -> existing[name] ?: CategoryEntity(name = name) }

        val saved = categoryRepository.saveAll(categoriesToSave)
        println("Upserted ${saved.size} categories.")
        return saved
    }

    fun seedAccountProducts(): List<AccountProductEntity> {
        if (accountProductRepository.count() > 0L) {
            val accountProducts = accountProductRepository.findAll()
            println("Account products already exist. Skipping seeding.")
            return accountProducts
        }
        val description = "this card will give you a lot of perks and bonuses when used with our banking services and categories"

        // 1. Debit Card – No Credit, No Fees
        val debitAccount = AccountProductEntity(
            name = "Salary Account",
            accountType = AccountType.DEBIT,
            description = description,
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
            description = description,
            interestRate = BigDecimal("0.000"),
            minBalanceRequired = BigDecimal("0.000"),
            creditLimit = BigDecimal("0.000"),
            annualFee = BigDecimal("0.000"),
            minSalary = BigDecimal("0.000"),
            image = "http://localhost:9000/capstone-public/fd4ac1cd-c88f-446a-846b-f23ecea8fe63"
        )

        // 3. Cashback Wallet
        val cashBackAccount = AccountProductEntity(
            name = "Cashback Wallet",
            accountType = AccountType.CASHBACK,
            description = description,
            interestRate = BigDecimal("0.000"),
            minBalanceRequired = BigDecimal("0.000"),
            creditLimit = BigDecimal("0.000"),
            annualFee = BigDecimal("0.000"),
            minSalary = BigDecimal("0.000"),
            image = "http://localhost:9000/capstone-public/fd4ac1cd-c88f-446a-846b-f23ecea8fe63"
        )

        // BASIC TIER CARDS (3 cards)

        // 4. Family Essential Card - For families with children
        val familyCard = AccountProductEntity(
            name = "Family Essential",
            accountType = AccountType.CREDIT,
            description = "Perfect for families - earn rewards on groceries, education, healthcare and dining",
            interestRate = BigDecimal("2.8"),
            minBalanceRequired = BigDecimal("0.000"),
            creditLimit = BigDecimal("800.000"),
            annualFee = BigDecimal("30.000"),
            minSalary = BigDecimal("500.000"),
            image = "http://localhost:9000/capstone-public/fd4ac1cd-c88f-446a-846b-f23ecea8fe63"
        )

        // 5. Youth Starter Card - For young adults and students
        val youthCard = AccountProductEntity(
            name = "Youth Starter",
            accountType = AccountType.CREDIT,
            description = "Designed for young adults - rewards on technology, fashion, dining and entertainment",
            interestRate = BigDecimal("3.2"),
            minBalanceRequired = BigDecimal("0.000"),
            creditLimit = BigDecimal("600.000"),
            annualFee = BigDecimal("20.000"),
            minSalary = BigDecimal("350.000"),
            image = "http://localhost:9000/capstone-public/fd4ac1cd-c88f-446a-846b-f23ecea8fe63"
        )

        // 6. Shopper's Delight Card - For retail enthusiasts
        val shopperCard = AccountProductEntity(
            name = "Shopper's Delight",
            accountType = AccountType.CREDIT,
            description = "Maximize savings on retail, fashion, wholesale and personal purchases",
            interestRate = BigDecimal("2.9"),
            minBalanceRequired = BigDecimal("0.000"),
            creditLimit = BigDecimal("1200.000"),
            annualFee = BigDecimal("40.000"),
            minSalary = BigDecimal("600.000"),
            image = "http://localhost:9000/capstone-public/fd4ac1cd-c88f-446a-846b-f23ecea8fe63"
        )

        // MIDDLE TIER CARDS (3 cards)

        // 7. Travel Explorer Card - For frequent travelers
        val travelCard = AccountProductEntity(
            name = "Travel Explorer",
            accountType = AccountType.CREDIT,
            description = "Perfect for travelers - premium rewards on hospitality, dining, automotive and logistics",
            interestRate = BigDecimal("2.3"),
            minBalanceRequired = BigDecimal("0.000"),
            creditLimit = BigDecimal("2500.000"),
            annualFee = BigDecimal("75.000"),
            minSalary = BigDecimal("900.000"),
            image = "http://localhost:9000/capstone-public/fd4ac1cd-c88f-446a-846b-f23ecea8fe63"
        )

        // 8. Business Pro Card - For entrepreneurs and business owners
        val businessProCard = AccountProductEntity(
            name = "Business Pro",
            accountType = AccountType.CREDIT,
            description = "Tailored for business needs - rewards on consulting, financial services, technology and manufacturing",
            interestRate = BigDecimal("2.1"),
            minBalanceRequired = BigDecimal("0.000"),
            creditLimit = BigDecimal("3000.000"),
            annualFee = BigDecimal("85.000"),
            minSalary = BigDecimal("1200.000"),
            image = "http://localhost:9000/capstone-public/fd4ac1cd-c88f-446a-846b-f23ecea8fe63"
        )

        // 9. Lifestyle Premium Card - For health and fashion conscious
        val lifestyleCard = AccountProductEntity(
            name = "Lifestyle Premium",
            accountType = AccountType.CREDIT,
            description = "For the style and health conscious - rewards on healthcare, fashion, personal care and hospitality",
            interestRate = BigDecimal("2.4"),
            minBalanceRequired = BigDecimal("0.000"),
            creditLimit = BigDecimal("2200.000"),
            annualFee = BigDecimal("65.000"),
            minSalary = BigDecimal("800.000"),
            image = "http://localhost:9000/capstone-public/fd4ac1cd-c88f-446a-846b-f23ecea8fe63"
        )

        // HIGH TIER CARDS (2 cards)

        // 10. Platinum Elite Card - For high earners
        val platinumCard = AccountProductEntity(
            name = "Platinum Elite",
            accountType = AccountType.CREDIT,
            description = "Exclusive benefits for high earners - premium rewards across real estate, automotive, energy, consulting and financial services",
            interestRate = BigDecimal("1.8"),
            minBalanceRequired = BigDecimal("0.000"),
            creditLimit = BigDecimal("6000.000"),
            annualFee = BigDecimal("150.000"),
            minSalary = BigDecimal("1800.000"),
            image = "http://localhost:9000/capstone-public/fd4ac1cd-c88f-446a-846b-f23ecea8fe63"
        )

        // 11. Diamond VIP Card - For VIP clients and ultra-high spenders
        val diamondCard = AccountProductEntity(
            name = "Diamond VIP",
            accountType = AccountType.CREDIT,
            description = "Ultimate luxury card for VIP clients - exceptional rewards across all premium categories including real estate, automotive, hospitality, energy and consulting",
            interestRate = BigDecimal("1.5"),
            minBalanceRequired = BigDecimal("0.000"),
            creditLimit = BigDecimal("10000.000"),
            annualFee = BigDecimal("250.000"),
            minSalary = BigDecimal("2500.000"),
            image = "http://localhost:9000/capstone-public/fd4ac1cd-c88f-446a-846b-f23ecea8fe63"
        )

        val accountProductEntities = accountProductRepository.saveAll(listOf(
            debitAccount, businessAccount, cashBackAccount,
            familyCard, youthCard, shopperCard,
            travelCard, businessProCard, lifestyleCard,
            platinumCard, diamondCard
        ))
        println("Seeded enhanced account products with specialized credit cards.")
        return accountProductEntities
    }

    fun seedPerks(accountProducts: List<AccountProductEntity>, categories: List<CategoryEntity>) {
        if (perkRepository.count() > 0L) {
            println("Perks already exist. Skipping seeding.")
            return
        }

        val perks = mutableListOf<PerkEntity>()

        // BASIC TIER PERKS

        // Family Essential Card Perks
        val familyCard = accountProducts.first { it.name == "Family Essential" }
        perks.addAll(listOf(
            PerkEntity(
                type = RewardType.CASHBACK,
                minPayment = BigDecimal("25.00"),
                rewardsXp = 12,
                perkAmount = BigDecimal("3.0"),
                isTierBased = false,
                accountProduct = familyCard,
                categories = categories.filter { it.name in listOf("retail", "wholesale") }.toMutableList()
            ),
            PerkEntity(
                type = RewardType.DISCOUNT,
                minPayment = BigDecimal("40.00"),
                rewardsXp = 15,
                perkAmount = BigDecimal("5.0"),
                isTierBased = false,
                accountProduct = familyCard,
                categories = categories.filter { it.name in listOf("education", "healthcare") }.toMutableList()
            ),
            PerkEntity(
                type = RewardType.CASHBACK,
                minPayment = BigDecimal("20.00"),
                rewardsXp = 10,
                perkAmount = BigDecimal("2.5"),
                isTierBased = false,
                accountProduct = familyCard,
                categories = categories.filter { it.name in listOf("dining") }.toMutableList()
            )
        ))

        // Youth Starter Card Perks
        val youthCard = accountProducts.first { it.name == "Youth Starter" }
        perks.addAll(listOf(
            PerkEntity(
                type = RewardType.CASHBACK,
                minPayment = BigDecimal("15.00"),
                rewardsXp = 8,
                perkAmount = BigDecimal("2.0"),
                isTierBased = false,
                accountProduct = youthCard,
                categories = categories.filter { it.name in listOf("technology", "fashion") }.toMutableList()
            ),
            PerkEntity(
                type = RewardType.DISCOUNT,
                minPayment = BigDecimal("30.00"),
                rewardsXp = 12,
                perkAmount = BigDecimal("4.0"),
                isTierBased = false,
                accountProduct = youthCard,
                categories = categories.filter { it.name in listOf("dining") }.toMutableList()
            )
        ))

        // Shopper's Delight Card Perks
        val shopperCard = accountProducts.first { it.name == "Shopper's Delight" }
        perks.addAll(listOf(
            PerkEntity(
                type = RewardType.CASHBACK,
                minPayment = BigDecimal("30.00"),
                rewardsXp = 15,
                perkAmount = BigDecimal("4.5"),
                isTierBased = true,
                accountProduct = shopperCard,
                categories = categories.filter { it.name in listOf("retail", "fashion", "wholesale") }.toMutableList()
            ),
            PerkEntity(
                type = RewardType.DISCOUNT,
                minPayment = BigDecimal("50.00"),
                rewardsXp = 18,
                perkAmount = BigDecimal("6.0"),
                isTierBased = true,
                accountProduct = shopperCard,
                categories = categories.filter { it.name in listOf("retail") }.toMutableList()
            )
        ))

        // MIDDLE TIER PERKS

        // Travel Explorer Card Perks
        val travelCard = accountProducts.first { it.name == "Travel Explorer" }
        perks.addAll(listOf(
            PerkEntity(
                type = RewardType.CASHBACK,
                minPayment = BigDecimal("50.00"),
                rewardsXp = 20,
                perkAmount = BigDecimal("6.0"),
                isTierBased = true,
                accountProduct = travelCard,
                categories = categories.filter { it.name in listOf("hospitality", "automotive") }.toMutableList()
            ),
            PerkEntity(
                type = RewardType.DISCOUNT,
                minPayment = BigDecimal("75.00"),
                rewardsXp = 25,
                perkAmount = BigDecimal("8.0"),
                isTierBased = true,
                accountProduct = travelCard,
                categories = categories.filter { it.name in listOf("dining", "logistics") }.toMutableList()
            ),
            PerkEntity(
                type = RewardType.CASHBACK,
                minPayment = BigDecimal("40.00"),
                rewardsXp = 18,
                perkAmount = BigDecimal("4.5"),
                isTierBased = false,
                accountProduct = travelCard,
                categories = categories.filter { it.name in listOf("energy") }.toMutableList()
            )
        ))

        // Business Pro Card Perks
        val businessProCard = accountProducts.first { it.name == "Business Pro" }
        perks.addAll(listOf(
            PerkEntity(
                type = RewardType.CASHBACK,
                minPayment = BigDecimal("100.00"),
                rewardsXp = 30,
                perkAmount = BigDecimal("7.5"),
                isTierBased = true,
                accountProduct = businessProCard,
                categories = categories.filter { it.name in listOf("consulting", "financial services") }.toMutableList()
            ),
            PerkEntity(
                type = RewardType.DISCOUNT,
                minPayment = BigDecimal("80.00"),
                rewardsXp = 25,
                perkAmount = BigDecimal("6.5"),
                isTierBased = true,
                accountProduct = businessProCard,
                categories = categories.filter { it.name in listOf("technology", "manufacturing") }.toMutableList()
            ),
            PerkEntity(
                type = RewardType.CASHBACK,
                minPayment = BigDecimal("60.00"),
                rewardsXp = 22,
                perkAmount = BigDecimal("5.0"),
                isTierBased = false,
                accountProduct = businessProCard,
                categories = categories.filter { it.name in listOf("logistics", "wholesale") }.toMutableList()
            )
        ))

        // Lifestyle Premium Card Perks
        val lifestyleCard = accountProducts.first { it.name == "Lifestyle Premium" }
        perks.addAll(listOf(
            PerkEntity(
                type = RewardType.CASHBACK,
                minPayment = BigDecimal("45.00"),
                rewardsXp = 20,
                perkAmount = BigDecimal("5.5"),
                isTierBased = true,
                accountProduct = lifestyleCard,
                categories = categories.filter { it.name in listOf("healthcare", "fashion") }.toMutableList()
            ),
            PerkEntity(
                type = RewardType.DISCOUNT,
                minPayment = BigDecimal("60.00"),
                rewardsXp = 25,
                perkAmount = BigDecimal("7.0"),
                isTierBased = true,
                accountProduct = lifestyleCard,
                categories = categories.filter { it.name in listOf("personal", "hospitality") }.toMutableList()
            ),
            PerkEntity(
                type = RewardType.CASHBACK,
                minPayment = BigDecimal("35.00"),
                rewardsXp = 15,
                perkAmount = BigDecimal("3.5"),
                isTierBased = false,
                accountProduct = lifestyleCard,
                categories = categories.filter { it.name in listOf("dining") }.toMutableList()
            )
        ))

        // HIGH TIER PERKS

        // Platinum Elite Card Perks
        val platinumCard = accountProducts.first { it.name == "Platinum Elite" }
        perks.addAll(listOf(
            PerkEntity(
                type = RewardType.CASHBACK,
                minPayment = BigDecimal("150.00"),
                rewardsXp = 40,
                perkAmount = BigDecimal("10.0"),
                isTierBased = true,
                accountProduct = platinumCard,
                categories = categories.filter { it.name in listOf("real estate", "automotive") }.toMutableList()
            ),
            PerkEntity(
                type = RewardType.DISCOUNT,
                minPayment = BigDecimal("120.00"),
                rewardsXp = 35,
                perkAmount = BigDecimal("12.0"),
                isTierBased = true,
                accountProduct = platinumCard,
                categories = categories.filter { it.name in listOf("energy", "consulting") }.toMutableList()
            ),
            PerkEntity(
                type = RewardType.CASHBACK,
                minPayment = BigDecimal("100.00"),
                rewardsXp = 30,
                perkAmount = BigDecimal("8.0"),
                isTierBased = true,
                accountProduct = platinumCard,
                categories = categories.filter { it.name in listOf("financial services", "hospitality") }.toMutableList()
            ),
            PerkEntity(
                type = RewardType.DISCOUNT,
                minPayment = BigDecimal("80.00"),
                rewardsXp = 25,
                perkAmount = BigDecimal("6.0"),
                isTierBased = false,
                accountProduct = platinumCard,
                categories = categories.filter { it.name in listOf("technology", "dining") }.toMutableList()
            )
        ))

        // Diamond VIP Card Perks
        val diamondCard = accountProducts.first { it.name == "Diamond VIP" }
        perks.addAll(listOf(
            PerkEntity(
                type = RewardType.CASHBACK,
                minPayment = BigDecimal("200.00"),
                rewardsXp = 50,
                perkAmount = BigDecimal("15.0"),
                isTierBased = true,
                accountProduct = diamondCard,
                categories = categories.filter { it.name in listOf("real estate", "automotive", "energy") }.toMutableList()
            ),
            PerkEntity(
                type = RewardType.DISCOUNT,
                minPayment = BigDecimal("180.00"),
                rewardsXp = 45,
                perkAmount = BigDecimal("18.0"),
                isTierBased = true,
                accountProduct = diamondCard,
                categories = categories.filter { it.name in listOf("hospitality", "consulting") }.toMutableList()
            ),
            PerkEntity(
                type = RewardType.CASHBACK,
                minPayment = BigDecimal("150.00"),
                rewardsXp = 40,
                perkAmount = BigDecimal("12.0"),
                isTierBased = true,
                accountProduct = diamondCard,
                categories = categories.filter { it.name in listOf("financial services", "technology") }.toMutableList()
            ),
            PerkEntity(
                type = RewardType.DISCOUNT,
                minPayment = BigDecimal("120.00"),
                rewardsXp = 35,
                perkAmount = BigDecimal("10.0"),
                isTierBased = true,
                accountProduct = diamondCard,
                categories = categories.filter { it.name in listOf("fashion", "healthcare", "manufacturing") }.toMutableList()
            ),
            PerkEntity(
                type = RewardType.CASHBACK,
                minPayment = BigDecimal("100.00"),
                rewardsXp = 30,
                perkAmount = BigDecimal("8.0"),
                isTierBased = false,
                accountProduct = diamondCard,
                categories = categories.filter { it.name in listOf("dining", "retail") }.toMutableList()
            )
        ))

        perkRepository.saveAll(perks)
        println("Seeded ${perks.size} specialized perks across all card tiers.")
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

        val categoriesMap = categories.associateBy { it.name }

        val partnerToCategory = listOf(

            "Jumeirah Hotels" to categoriesMap.get("hospitality")!!,
            "Almosafer" to categoriesMap.get("travel")!!,
            "Caribou Coffee" to categoriesMap.get("dining")!!,
            "Shake Shack" to categoriesMap.get("dining")!!,
            "KidZania Kuwait" to categoriesMap.get("education")!!,
            "VOX Cinemas" to categoriesMap.get("entertainment")!!,
            "Kuwait Airways" to categoriesMap.get("travel")!!,
            "Xcite Electronics" to categoriesMap.get("technology")!!,
            "H&M" to categoriesMap.get("fashion")!!,
            "Safat Home" to categoriesMap.get("retail")!!,
            "Spark Gym" to categoriesMap.get("personal care")!!,
            "Pick" to categoriesMap.get("dining")!!,
            "OFK" to categoriesMap.get("dining")!!,
            "Grand Cinemas" to categoriesMap.get("entertainment")!!,
            "Chips Store" to categoriesMap.get("technology")!!,
            "Sultan Center" to categoriesMap.get("wholesale")!!
        )

       // val logisticsoBase = "http://localhost:9000/capstone-public/"

        val businessPartners = partnerToCategory.mapIndexed { i, (name, category) ->
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
                logoUrl = "${name.lowercase().replace(" ", "_")}.png",
                category = category
            )
        }

        accountRepository.saveAll(businessPartners.map { it.account!! })
        businessPartnerRepository.saveAll(businessPartners)
        println("Seeded ${businessPartners.size} business partners.")
    }

    fun seedTiers() {
        if (xpTierRepository.count() > 0L) {
            println("XP Tiers already exist. Skipping seeding.")
            return
        }
        val tiers = listOf(
            XpTierEntity(
                name = "Silver",
                minXp = 0,
                maxXp = 999,
                xpPerkMultiplier = 1.0,
                xpPerNotification = 10,
                xpPerPromotion = 20,
                perkAmountPercentage = 5
            ),
            XpTierEntity(
                name = "Gold",
                minXp = 1000,
                maxXp = 4999,
                xpPerkMultiplier = 1.2,
                xpPerNotification = 15,
                xpPerPromotion = 25,
                perkAmountPercentage = 10
            ),
            XpTierEntity(
                name = "Platinum",
                minXp = 5000,
                maxXp = 999999,
                xpPerkMultiplier = 1.5,
                xpPerNotification = 20,
                xpPerPromotion = 30,
                perkAmountPercentage = 15
            )
        )
        xpTierRepository.saveAll(tiers)
        println("Seeded XP Tiers: ${tiers.joinToString { it.name.toString() }}.")
    }
}
