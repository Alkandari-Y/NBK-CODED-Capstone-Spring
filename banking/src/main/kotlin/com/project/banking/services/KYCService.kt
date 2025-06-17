package com.project.banking.services

import com.project.banking.entities.KycEntity
import com.project.common.data.requests.kyc.KYCRequest
import com.project.common.data.responses.authentication.UserInfoDto


interface KYCService {
    fun createKYCOrUpdate(kycRequest: KYCRequest, user: UserInfoDto): KycEntity
    fun findKYCByUserId(userId: Long): KycEntity?
}