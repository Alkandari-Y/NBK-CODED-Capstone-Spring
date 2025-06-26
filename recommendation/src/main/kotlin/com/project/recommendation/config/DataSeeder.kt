package com.project.recommendation.config

import com.project.recommendation.entities.StoreLocationEntity
import com.project.recommendation.repositories.StoreLocationRepository
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.time.LocalTime
import java.time.format.DateTimeFormatter


@Component
class DataSeeder(
    private val storeLocationRepository: StoreLocationRepository
) {
    @EventListener(ApplicationReadyEvent::class)
    @Transactional
    fun seedInitialData() {
        println("Delaying data seeding for 5 seconds...")
        Thread.sleep(8000)
        println("Starting data seeding...")
//        seedStoreLocations()

    }

    fun seedStoreLocations() {
        if (storeLocationRepository.count() > 0L) {
            println("Business partners already exist. Skipping seeding.")
            return
        }

        println("Seeded default locations.")
        val jumeirahHotel = StoreLocationEntity(
            partnerId = 1, // reflects partner id
            longitude = BigDecimal("29.268885427648737"),
            latitude = BigDecimal("48.08888399119982"),
            googleMapUrl = "https://maps.app.goo.gl/MpCyymSZptJc4W9x5",
            country = "kuwait",
            addressLine1 = "Messilah",
            addressLine2 = "Al Ta’awun Street, Kuwait",
            opensAt = "08:00 AM".toLocalTime(),
            closesAt = "02:00 AM".toLocalTime(),
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
            opensAt = "10:00 AM".toLocalTime(),
            closesAt = "10:00 AM".toLocalTime(),
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
            opensAt = "06:00 AM".toLocalTime(),
            closesAt = "11:30 AM".toLocalTime(),
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
            opensAt = "11:00 AM".toLocalTime(),
            closesAt = "11:00 AM".toLocalTime(),
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
            opensAt = "10:00 AM".toLocalTime(),
            closesAt = "10:00 AM".toLocalTime(),
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
            opensAt = "11:00 AM".toLocalTime(),
            closesAt = "03:30 PM".toLocalTime(),
            beaconId = 1,
        )
        val kuwaitAirways = StoreLocationEntity(
            partnerId = 7, // reflects partner id
            longitude = BigDecimal("29.256278021380403"),
            latitude = BigDecimal("47.96930350563195"),
            googleMapUrl = "https://maps.app.goo.gl/58Fz1qcrb8r7Tjp9A",
            country = "Kuwait",
            addressLine1 = "Farwaniya",
            addressLine2 = "Dajeej",
            opensAt = "07:00 AM".toLocalTime(),
            closesAt = "03:00 PM".toLocalTime(),
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
            opensAt = "09:00 AM".toLocalTime(),
            closesAt = "11:00 PM".toLocalTime(),
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
            opensAt = "10:00 AM".toLocalTime(),
            closesAt = "12:00 AM".toLocalTime(),
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
            opensAt = "09:30 AM".toLocalTime(),
            closesAt = "12:00 AM".toLocalTime(),
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
            opensAt = "09:30 AM".toLocalTime(),
            closesAt = "12:00 AM".toLocalTime(),
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
            opensAt = "06:30 AM".toLocalTime(),
            closesAt = "11:00 PM".toLocalTime(),
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
            opensAt = "01:00 PM".toLocalTime(),
            closesAt = "11:00 PM".toLocalTime(),
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
            opensAt = "11:00 AM".toLocalTime(),
            closesAt = "03:30 PM".toLocalTime(),
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
            opensAt = "10:00 AM".toLocalTime(),
            closesAt = "10:00 PM".toLocalTime(),
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
            opensAt = "08:00 AM".toLocalTime(),
            closesAt = "12:00 AM".toLocalTime(),
            beaconId = 1,
        )

        storeLocationRepository.saveAll(listOf(
            pickAlHamra
        ))


    }

}
fun String.toLocalTime(): LocalTime {

    return LocalTime.parse(this, DateTimeFormatter.ofPattern("HH:mm: a"))
}