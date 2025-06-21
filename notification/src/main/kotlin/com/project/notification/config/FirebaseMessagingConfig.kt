package com.project.notification.config

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.messaging.FirebaseMessaging
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.io.ByteArrayInputStream

@Configuration
class FirebaseMessagingConfig {

    @Bean
    fun firebaseApp(): FirebaseApp {
        if (FirebaseApp.getApps().isNotEmpty()) {
            return FirebaseApp.getInstance()
        }

        val privateKey = System.getenv("FIREBASE_PRIVATE_KEY")?.replace("\\n", "\n")

        val firebaseJson = """
        {
            "type": "service_account",
            "project_id": "${System.getenv("FIREBASE_PROJECT_ID")}",
            "private_key_id": "${System.getenv("FIREBASE_PROJECT_KEY_ID")}",
            "private_key": "-----BEGIN PRIVATE KEY-----\n$privateKey\n-----END PRIVATE KEY-----\n",
            "client_email": "firebase-adminsdk-fbsvc@${System.getenv("FIREBASE_PROJECT_ID")}.iam.gserviceaccount.com",
            "client_id": "${System.getenv("FIREBASE_CLIENT_ID")}",
            "auth_uri": "https://accounts.google.com/o/oauth2/auth",
            "token_uri": "https://oauth2.googleapis.com/token",
            "auth_provider_x509_cert_url": "https://www.googleapis.com/oauth2/v1/certs",
            "client_x509_cert_url": "https://www.googleapis.com/robot/v1/metadata/x509/firebase-adminsdk-fbsvc@${System.getenv("FIREBASE_PROJECT_ID")}.iam.gserviceaccount.com",
            "universe_domain": "googleapis.com"
        }
        """.trimIndent()

        val credentialsStream = ByteArrayInputStream(firebaseJson.toByteArray(Charsets.UTF_8))
        val options = FirebaseOptions.builder()
            .setCredentials(GoogleCredentials.fromStream(credentialsStream))
            .build()

        return FirebaseApp.initializeApp(options)
    }

    @Bean
    fun firebaseMessaging(firebaseApp: FirebaseApp): FirebaseMessaging {
        return FirebaseMessaging.getInstance(firebaseApp)
    }
}
