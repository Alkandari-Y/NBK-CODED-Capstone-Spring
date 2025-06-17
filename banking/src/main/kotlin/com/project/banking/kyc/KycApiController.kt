package com.project.banking.kyc

import com.project.banking.mappers.toResponse
import com.project.banking.services.KYCService
import com.project.common.data.requests.kyc.KYCRequest
import com.project.common.data.responses.authentication.UserInfoDto
import com.project.common.data.responses.kyc.KYCResponse
import com.project.common.exceptions.kyc.KycNotFoundException
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestAttribute
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/kyc")
class KycApiController(
    private val kycService: KYCService,
) {
    @PostMapping
    fun createOrUpdateKYC(
        @Valid @RequestBody kycRequest: KYCRequest,
        @RequestAttribute("authUser") authUser: UserInfoDto,
    ): ResponseEntity<KYCResponse> {
        val kyc = kycService.createKYCOrUpdate(
            kycRequest = kycRequest,
            user = authUser,
        ).toResponse()

        return ResponseEntity(
            kyc,
            HttpStatus.CREATED
        )
    }

    @GetMapping
    fun getKYCBy(
        @RequestAttribute("authUser") authUser: UserInfoDto,
    ): ResponseEntity<KYCResponse> {

        val kyc = kycService.findKYCByUserId(authUser.userId)
            ?: throw KycNotFoundException(authUser.userId)

        return ResponseEntity(
            kyc.toResponse(),
            HttpStatus.OK
        )
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(path = ["/client/{userId}"])
    fun getKYCByUserId(
        @PathVariable("userId") userId: Long
    ): ResponseEntity<KYCResponse> {

        val kyc = kycService.findKYCByUserId(userId)
            ?: throw KycNotFoundException(userId)

        return ResponseEntity(
            kyc.toResponse(),
            HttpStatus.OK
        )
    }
}