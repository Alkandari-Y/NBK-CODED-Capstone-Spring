package com.project.banking.services

import com.project.common.enums.ErrorCode
import com.project.common.exceptions.APIException
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.services.s3.model.GetObjectRequest
import software.amazon.awssdk.services.s3.presigner.S3Presigner
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest
import java.util.UUID
import kotlin.time.Duration

@Service
class FileStorageService(
    private val s3Client: S3Client,
    @Value("\${aws.bucket.public}") private val publicBucket: String,
) {

    fun uploadFile(file: MultipartFile, isPublic: Boolean): Pair<String, String> {
        val bucket = publicBucket
        val key = UUID.randomUUID().toString()

        val putRequest = PutObjectRequest.builder()
            .bucket(bucket)
            .key(key)
            .contentType(file.contentType)
            .build()

        s3Client.putObject(putRequest, RequestBody.fromBytes(file.bytes))

        return Pair(bucket, key)
    }
}