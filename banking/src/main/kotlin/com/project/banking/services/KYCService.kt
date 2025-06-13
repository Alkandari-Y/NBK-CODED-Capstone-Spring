package com.project.banking.services

import com.project.banking.entities.KYCEntity
import com.project.common.data.requests.kyc.KYCRequest
import com.project.common.data.responses.authentication.UserInfoDto


interface KYCService {
    fun createKYCOrUpdate(kycRequest: KYCRequest, user: UserInfoDto): KYCEntity
    fun findKYCByUserId(userId: Long): KYCEntity?
}