package com.project.recommendation.config

import com.project.common.enums.AccountType
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal


@Component
class DataSeeder(

) {
    @EventListener(ApplicationReadyEvent::class)
    @Transactional
    fun seedInitialData() {
        seedStoreLocations()

    }


    fun seedStoreLocations() {
        println("Seeded default locations.")
    }

}
