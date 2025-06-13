package com.project.banking.services

import jakarta.mail.internet.MimeMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service

@Service
class MailService(
    private val mailSender: JavaMailSender
) {
    fun sendHtmlEmail(to: String, subject: String, username: String, bodyText: String) {
        val htmlBody = emailTemplate
            .replace("\${username}", username)
            .replace("\${body}", bodyText)
        val message: MimeMessage = mailSender.createMimeMessage()
        val helper = MimeMessageHelper(message, "utf-8")
        helper.setTo(to)
        helper.setSubject(subject)
        helper.setText(htmlBody, true)
        helper.setFrom("noreply@loanlift.com")

        mailSender.send(message)
    }

    val emailTemplate = """
        <!DOCTYPE html>
        <html lang="en">
        <head>
          <meta charset="UTF-8" />
          <style>
            body {
              font-family: 'Segoe UI', sans-serif;
              background-color: #f4f4f4;
              padding: 20px;
              margin: 0;
            }

            .container {
              max-width: 600px;
              margin: 0 auto;
              background-color: #ffffff;
              border-radius: 8px;
              padding: 20px;
              box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
            }

            .header {
              background-color: #0077cc;
              color: white;
              padding: 15px;
              text-align: center;
              font-size: 20px;
              border-radius: 6px 6px 0 0;
            }

            .content {
              margin-top: 15px;
              font-size: 16px;
              color: #333333;
            }

            .footer {
              margin-top: 30px;
              font-size: 12px;
              color: #999999;
              text-align: center;
            }

            a.button {
              display: inline-block;
              background-color: #0077cc;
              color: white;
              padding: 10px 20px;
              margin-top: 20px;
              text-decoration: none;
              border-radius: 4px;
            }

            a.button:hover {
              background-color: #005fa3;
            }
          </style>
        </head>
        <body>
          <div class="container">
            <div class="header">LoanLift Notification</div>

            <div class="content">
              <p>Hi ${'$'}{username},</p>
              <p>${'$'}{body}</p>
            </div>
            <div class="footer">
              &copy; 2025 LoanLift. All rights reserved.
            </div>
          </div>
        </body>
        </html>
    """.trimIndent()
}
