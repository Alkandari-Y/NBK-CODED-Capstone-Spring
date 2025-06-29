package com.project.recommendation.tasks

import com.project.recommendation.repositories.PromotionRepository
import com.project.recommendation.services.PromotionService
import jakarta.transaction.Transactional
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class PromotionLastDayNotification(
    private val promotionRepository: PromotionRepository
) {

    // Runs at 2:00 AM on the 22nd day of every month (Asia/Kuwait time)
    // Second Minute Hours DayOfMonth Month DayOfWeek
    //   0      0       0       0       0       ?
//    @Scheduled(cron = "0 30 14 13 5 ?", zone = "Asia/Kuwait") // for demo
    @Scheduled(cron = "0 1 9 * * ?", zone = "Asia/Kuwait")
    fun processPromotionsEndingToday() {
        println("[Scheduler] searching for promotions ending today...")
//        val promotionsEndingToday =
    }

    // TODO("trigger notifcations to groups")


}