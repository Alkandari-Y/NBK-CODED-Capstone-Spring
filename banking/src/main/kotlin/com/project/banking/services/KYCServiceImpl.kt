package com.project.banking.services

import com.project.common.exceptions.accounts.AccountVerificationException
import com.project.banking.entities.KycEntity
import com.project.banking.events.KycCreatedEvent
import com.project.banking.mappers.toEntity
import com.project.banking.repositories.KYCRepository
import com.project.common.data.requests.kyc.KYCRequest
import com.project.common.data.responses.authentication.UserInfoDto
import com.project.common.enums.ErrorCode
import com.project.common.utils.dateFormatter
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.Period

@Service
class KYCServiceImpl(
    private val kycRepository: KYCRepository,
    private val mailService: MailService,
    private val publisher: ApplicationEventPublisher

): KYCService {

    override fun createKYCOrUpdate(
        kycRequest: KYCRequest,
        user: UserInfoDto
    ): KycEntity {
        val existingKYC = kycRepository.findByUserId(user.userId)

        val newKycEntity  = existingKYC?.copy(
            firstName= kycRequest.firstName,
            lastName= kycRequest.lastName,
            dateOfBirth= kycRequest.dateOfBirth.let {
                LocalDate.parse(kycRequest.dateOfBirth, dateFormatter)
            },
            nationality= kycRequest.nationality,
            salary= kycRequest.salary,
            civilId = kycRequest.civilId,
            mobileNumber = kycRequest.mobileNumber,
        )
            ?: kycRequest.toEntity(user.userId)

        val currentDate = LocalDate.now()
        val yearsOfAge = Period.between(newKycEntity.dateOfBirth, currentDate).years
        if (yearsOfAge < 18) throw AccountVerificationException(
            message = "User must be 18 or older",
            code = ErrorCode.INVALID_AGE
        )

        val savedKyc = kycRepository.save(newKycEntity)
        publisher.publishEvent(KycCreatedEvent(this, savedKyc))

//        mailService.sendHtmlEmail(
//            to = user.email,
//            subject = "Account Activated",
//            username = user.username,
//            bodyText = "Your account has been activated! Please enjoy our services"
//        )
        return savedKyc
    }

    override fun findKYCByUserId(userId: Long): KycEntity? {
        return kycRepository.findByUserId(userId)
    }
}