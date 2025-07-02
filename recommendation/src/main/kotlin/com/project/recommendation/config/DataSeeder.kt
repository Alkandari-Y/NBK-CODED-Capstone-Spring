package com.project.recommendation.config

import com.project.recommendation.entities.StoreLocationEntity
import com.project.recommendation.repositories.StoreLocationRepository
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.time.LocalTime
import com.project.recommendation.repositories.PromotionRepository
import com.project.recommendation.entities.PromotionEntity
import com.project.common.enums.RewardType
import java.time.LocalDate


@Component
class DataSeeder(
    private val storeLocationRepository: StoreLocationRepository,
    private val promotionRepository: PromotionRepository
) {
    @EventListener(ApplicationReadyEvent::class)
    @Transactional
    fun seedInitialData() {
        println("Delaying data seeding for 5 seconds...")
        Thread.sleep(8000)
        println("Starting data seeding...")
        seedStoreLocations()
        seedPromotions()
    }

    fun seedStoreLocations() {
        if (storeLocationRepository.count() > 0L) {
            println("Business locations already exist. Skipping seeding.")
            return
        }

        println("Seeded default locations.")
        val jumeirahHotel = StoreLocationEntity(
            partnerId = 1,
            longitude = BigDecimal("29.268885427648737"),
            latitude = BigDecimal("48.08888399119982"),
            googleMapUrl = "https://maps.app.goo.gl/MpCyymSZptJc4W9x5",
            country = "kuwait",
            addressLine1 = "Messilah",
            addressLine2 = "Al Ta’awun Street, Kuwait",
            opensAt = LocalTime.parse("08:00"),
            closesAt = LocalTime.parse("02:00"),
            beaconId = 1,
        )
        val alMosafer = StoreLocationEntity(
            partnerId = 2, // reflects partner id
            longitude = BigDecimal("29.30386362725354"),
            latitude = BigDecimal("47.93920228034512"),
            googleMapUrl = "https://maps.app.goo.gl/kXHJNeNBK7MQQ8yh8",
            country = "Kuwait",
            addressLine1 = "Farwaniya",
            addressLine2 = "Ghazali St, Kuwait",
            opensAt = LocalTime.parse("10:00"),
            closesAt = LocalTime.parse("10:00"),
            beaconId = 1,
        )
        val caribouArraya = StoreLocationEntity(
            partnerId = 3, // reflects partner id
            longitude = BigDecimal("29.377097437910905"),
            latitude = BigDecimal("47.989935393232315"),
            googleMapUrl = "https://maps.app.goo.gl/5CTsFM3jpRPyTbt9A",
            country = "Kuwait",
            addressLine1 = "Kuwait City",
            addressLine2 = "Al-Rayah Tower, Al-Shuhada St",
            opensAt = LocalTime.parse("06:00"),
            closesAt = LocalTime.parse("11:30"),
            beaconId = 1,
        )
        val shakeShack = StoreLocationEntity(
            partnerId = 4, // reflects partner id
            longitude = BigDecimal("29.257698709807816"),
            latitude = BigDecimal("48.02211738757822"),
            googleMapUrl = "https://maps.app.goo.gl/b7EB3Wkq14STDEUf8",
            country = "Kuwait",
            addressLine1 = "Sabhan",
            addressLine2 = "The Murouj",
            opensAt = LocalTime.parse("11:00"),
            closesAt = LocalTime.parse("11:00"),
            beaconId = 1,
        )
        val kidZania = StoreLocationEntity(
            partnerId = 5, // reflects partner id
            longitude = BigDecimal("29.302511154213303"),
            latitude = BigDecimal("47.93607195584594"),
            googleMapUrl = "https://maps.app.goo.gl/GKKDXKWC5SQe7pS4A",
            country = "Kuwait",
            addressLine1 = "Al Rai",
            addressLine2 = "The Avenues Mall, 1st Floor",
            opensAt = LocalTime.parse("10:00"),
            closesAt = LocalTime.parse("10:00"),
            beaconId = 1,
        )
        val voxCinemas = StoreLocationEntity(
            partnerId = 6, // reflects partner id
            longitude = BigDecimal("29.304969010219697"),
            latitude = BigDecimal("47.943087696146556"),
            googleMapUrl = "https://maps.app.goo.gl/J6rNeykTS2XNZHtA6",
            country = "Kuwait",
            addressLine1 = "Al Rai",
            addressLine2 = "The Avenues Mall, Phase 4, 1st Floor",
            opensAt = LocalTime.parse("11:00"),
            closesAt = LocalTime.parse("03:30"),
            beaconId = 1821,
        )
        val kuwaitAirways = StoreLocationEntity(
            partnerId = 7, // reflects partner id
            longitude = BigDecimal("29.256278021380403"),
            latitude = BigDecimal("47.96930350563195"),
            googleMapUrl = "https://maps.app.goo.gl/58Fz1qcrb8r7Tjp9A",
            country = "Kuwait",
            addressLine1 = "Farwaniya",
            addressLine2 = "Dajeej",
            opensAt = LocalTime.parse("07:00"),
            closesAt = LocalTime.parse("15:00"),
            beaconId = 1,
        )
        val xcite = StoreLocationEntity(
            partnerId = 8, // reflects partner id
            longitude = BigDecimal("29.302421262580605"),
            latitude = BigDecimal("47.930135963921344"),
            googleMapUrl = "https://maps.app.goo.gl/AiqhJeDDgjwKYPeNA",
            country = "Kuwait",
            addressLine1 = "Al Rai",
            addressLine2 = "The Avenues Mall, Ground Floor",
            opensAt = LocalTime.parse("09:00"),
            closesAt = LocalTime.parse("11:00"),
            beaconId = 1,
        )
        val hAndM = StoreLocationEntity(
            partnerId = 9, // reflects partner id
            longitude = BigDecimal("29.37868952048407"),
            latitude = BigDecimal("47.99306350963311"),
            googleMapUrl = "https://maps.app.goo.gl/GwUkqAwFoNszC8YBA",
            country = "Kuwait",
            addressLine1 = "Al Rai",
            addressLine2 = "The Avenues Mall, Ground Floor",
            opensAt = LocalTime.parse("10:00"),
            closesAt = LocalTime.parse("12:00"),
            beaconId = 1,
        )
        val safatHome = StoreLocationEntity(
            partnerId = 10, // reflects partner id
            longitude = BigDecimal("29.31320516553269"),
            latitude = BigDecimal("47.93514936947917"),
            googleMapUrl = "https://maps.app.goo.gl/b4XxtbTmNUJJ66928",
            country = "Kuwait",
            addressLine1 = "Alrai",
            addressLine2 = "Fourth Ring Road St",
            opensAt = LocalTime.parse("09:30"),
            closesAt = LocalTime.parse("12:00"),
            beaconId = 1,
        )
        val sparkGym = StoreLocationEntity(
            partnerId = 11, // reflects partner id
            longitude = BigDecimal("29.31320516553269"),
            latitude = BigDecimal("47.93514936947917"),
            googleMapUrl = "https://maps.app.goo.gl/8fPJpnFSVUiiP9ok7",
            country = "Kuwait",
            addressLine1 = "Kuwait City",
            addressLine2 = "KIPCO Tower, Al-Shuhada St, Floor 2",
            opensAt = LocalTime.parse("09:30"),
            closesAt = LocalTime.parse("12:00"),
            beaconId = 1,
        )
        val pickAlHamra = StoreLocationEntity(
            partnerId = 12, // reflects partner id
            longitude = BigDecimal("29.37868972651343"),
            latitude = BigDecimal("47.99306473973293"),
            googleMapUrl = "https://maps.app.goo.gl/dS7T3fVUugiCVVRZ7",
            country = "Kuwait",
            addressLine1 = "Kuwait City",
            addressLine2 = "Al Hamra Tower, Al-Shuhada St",
            opensAt = LocalTime.parse("06:30"),
            closesAt = LocalTime.parse("11:00"),
            beaconId = 1,
        )
        val ofk = StoreLocationEntity(
            partnerId = 13, // reflects partner id
            longitude = BigDecimal("29.378674612438175"),
            latitude = BigDecimal("47.99265693723903"),
            googleMapUrl = "https://maps.app.goo.gl/H2KTLQ7aCoAWqQLJ9",
            country = "Kuwait",
            addressLine1 = "Kuwait City",
            addressLine2 = "Al Hamra Tower & Mall",
            opensAt = LocalTime.parse("01:00"),
            closesAt = LocalTime.parse("11:00"),
            beaconId = 1,
        )
        val grandCinemas = StoreLocationEntity(
            partnerId = 14, // reflects partner id
            longitude = BigDecimal("29.378701350821807"),
            latitude = BigDecimal("47.99298930468366"),
            googleMapUrl = "https://maps.app.goo.gl/76HqxAjQMqX4iwM16",
            country = "Kuwait",
            addressLine1 = "Kuwait City",
            addressLine2 = "Al Hamra Tower & Mall",
            opensAt = LocalTime.parse("11:00"),
            closesAt = LocalTime.parse("03:30"),
            beaconId = 1,
        )
        val chipsStore = StoreLocationEntity(
            partnerId = 15, // reflects partner id
            longitude = BigDecimal("29.378679376650446"),
            latitude = BigDecimal("47.99317651198166"),
            googleMapUrl = "https://maps.app.goo.gl/4pPx2qdyxdcFCzyZ8",
            country = "Kuwait",
            addressLine1 = "Kuwait City",
            addressLine2 = "Al Hamra Tower & Mall",
            opensAt = LocalTime.parse("10:00"),
            closesAt = LocalTime.parse("10:00"),
            beaconId = 1,
        )
        val sultanCenter = StoreLocationEntity(
            partnerId = 16, // reflects partner id
            longitude = BigDecimal("29.37897262009482"),
            latitude = BigDecimal("47.99318171311795"),
            googleMapUrl = "https://maps.app.goo.gl/XLYZqFAfjwZrWeSs5",
            country = "Kuwait",
            addressLine1 = "Kuwait City",
            addressLine2 = "Al Hamra Tower & Mall",
            opensAt = LocalTime.parse("08:00"),
            closesAt = LocalTime.parse("12:00"),
            beaconId = 1,
        )

        storeLocationRepository.saveAll(listOf(
            jumeirahHotel, alMosafer, caribouArraya, shakeShack, kidZania, voxCinemas, kuwaitAirways, xcite,
            hAndM, safatHome, sparkGym, pickAlHamra, ofk, grandCinemas, chipsStore, sultanCenter
        ))
    }
    fun seedPromotions() {
        if (promotionRepository.count() > 0L) {
            println("Promotions already exist. Skipping seeding.")
            return
        }

        println("Seeding default promotions.")

        val jumeirahPromotion = PromotionEntity(
            name = "Luxury Stay Package",
            businessPartnerId = 1,
            type = RewardType.DISCOUNT,
            startDate = LocalDate.parse("2025-07-01"),
            endDate = LocalDate.parse("2025-07-10"),
            description = "20% off Presidential Suite. Experience world-class hospitality.",
            storeId = 1
        )

        val almosaferPromotion = PromotionEntity(
            name = "Early Bird Flight Deals",
            businessPartnerId = 2,
            type = RewardType.DISCOUNT,
            startDate = LocalDate.parse("2025-07-02"),
            endDate = LocalDate.parse("2025-07-08"),
            description = "Book your summer vacation to Greece early and save up to 25%",
            storeId = 2
        )

        val caribouPromotion = PromotionEntity(
            name = "Icy Summer, Icy Coffee",
            businessPartnerId = 3,
            type = RewardType.DISCOUNT,
            startDate = LocalDate.parse("2025-07-01"),
            endDate = LocalDate.parse("2025-07-07"),
            description = "Buy a large iced beverage and get a free soft serve. Perfect to beat the heat!",
            storeId = 3
        )

        val shakeShackPromotion = PromotionEntity(
            name = "Burger & Shake Combo Deal",
            businessPartnerId = 4,
            type = RewardType.DISCOUNT,
            startDate = LocalDate.parse("2025-07-03"),
            endDate = LocalDate.parse("2025-07-09"),
            description = "Get 15% off when you order any burger with a shake!",
            storeId = 4
        )

        val voxPromotion = PromotionEntity(
            name = "Mission Impossible Movie Week",
            businessPartnerId = 6,
            type = RewardType.CASHBACK,
            startDate = LocalDate.parse("2025-07-04"),
            endDate = LocalDate.parse("2025-07-06"),
            description = "Get 10% cashback on tickets to Mission Impossible: Part Two. Make the impossible, possible!",
            storeId = 6
        )

        val kuwaitAirwaysPromotion = PromotionEntity(
            name = "Fly Kuwait Special Offer",
            businessPartnerId = 7,
            type = RewardType.DISCOUNT,
            startDate = LocalDate.parse("2025-07-02"),
            endDate = LocalDate.parse("2025-07-7"),
            description = "Special 20% discount on flights this weekend. Explore the world with comfort!",
            storeId = 7
        )

        val xcitePromotion = PromotionEntity(
            name = "Tech Summer Sale",
            businessPartnerId = 8,
            type = RewardType.DISCOUNT,
            startDate = LocalDate.parse("2025-07-01"),
            endDate = LocalDate.parse("2025-07-14"),
            description = "Up to 40% off on selected electronics. Upgrade your tech!",
            storeId = 8
        )


        val hAndMPromotion = PromotionEntity(
            name = "Summer Fashion Collection",
            businessPartnerId = 9,
            type = RewardType.DISCOUNT,
            startDate = LocalDate.parse("2025-06-28"),
            endDate = LocalDate.parse("2025-07-05"),
            description = "30% off on selected summer collection. Stay stylish and comfortable this season.",
            storeId = 9
        )

        val safatHomePromotion = PromotionEntity(
            name = "Home Makeover Special",
            businessPartnerId = 10,
            type = RewardType.CASHBACK,
            startDate = LocalDate.parse("2025-07-02"),
            endDate = LocalDate.parse("2025-07-16"),
            description = "Earn 10% cashback when spending over 100 KD on home decor and furniture purchases",
            storeId = 10
        )

        val sparkGymPromotion = PromotionEntity(
            name = "Summer Fitness Challenge",
            businessPartnerId = 11,
            type = RewardType.DISCOUNT,
            startDate = LocalDate.parse("2025-07-06"),
            endDate = LocalDate.parse("2025-07-13"),
            description = "Join our summer fitness program and get 20% off membership fees. Get summer-ready!",
            storeId = 11
        )


        val pickPromotion = PromotionEntity(
            name = "Healthy Meal Deals",
            businessPartnerId = 12,
            type = RewardType.DISCOUNT,
            startDate = LocalDate.parse("2025-07-04"),
            endDate = LocalDate.parse("2025-07-08"),
            description = "15% off on all healthy meal options. Eat well, Feel well",
            storeId = 12
        )

        val grandCinemasPromotion = PromotionEntity(
            name = "Family Movie Night",
            businessPartnerId = 14,
            type = RewardType.DISCOUNT,
            startDate = LocalDate.parse("2025-07-07"),
            endDate = LocalDate.parse("2025-07-10"),
            description = "25% off family tickets for selected shows.",
            storeId = 14
        )

        val chipsStorePromotion = PromotionEntity(
            name = "Gaming Gear Sale",
            businessPartnerId = 15,
            type = RewardType.CASHBACK,
            startDate = LocalDate.parse("2025-07-02"),
            endDate = LocalDate.parse("2025-07-09"),
            description = "Get 8% cashback on gaming accessories and tech gadgets. Level up your setup!",
            storeId = 15
        )

        val sultanCenterPromotion = PromotionEntity(
            name = "Bulk Shopping Rewards",
            businessPartnerId = 16,
            type = RewardType.CASHBACK,
            startDate = LocalDate.parse("2025-07-03"),
            endDate = LocalDate.parse("2025-07-17"),
            description = "Get 6% cashback on organic section and veggies purchases. Great deals on smart choices!",
            storeId = 16
        )

        promotionRepository.saveAll(listOf(
            jumeirahPromotion, almosaferPromotion, caribouPromotion, shakeShackPromotion, voxPromotion,
            kuwaitAirwaysPromotion, xcitePromotion, hAndMPromotion, safatHomePromotion, sparkGymPromotion, pickPromotion,
            grandCinemasPromotion, chipsStorePromotion, sultanCenterPromotion
        ))
    }
}