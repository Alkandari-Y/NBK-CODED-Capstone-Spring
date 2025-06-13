package com.project.authentication.services

import jakarta.mail.internet.MimeMessage
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentCaptor
import org.mockito.Mockito.*
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import kotlin.test.assertTrue
import kotlin.test.assertEquals

class MailServiceTest {

    private lateinit var mailSender: JavaMailSender
    private lateinit var mailService: MailService
    private lateinit var mimeMessage: MimeMessage

    @BeforeEach
    fun setup() {
        mailSender = mock(JavaMailSender::class.java)
        mimeMessage = mock(MimeMessage::class.java)
        `when`(mailSender.createMimeMessage()).thenReturn(mimeMessage)
        mailService = MailService(mailSender)
    }

    @Test
    fun `sendHtmlEmail should send email using JavaMailSender`() {
        // Arrange
        val to = "recipient@example.com"
        val subject = "Test Subject"
        val username = "TestUser"
        val bodyText = "Welcome to Capstone"

        // Act
        mailService.sendHtmlEmail(to, subject, username, bodyText)

        // Assert
        verify(mailSender).createMimeMessage()
        verify(mailSender).send(mimeMessage)
    }

}

