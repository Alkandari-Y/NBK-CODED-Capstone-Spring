package com.project.authentication.services

import com.project.common.data.requests.deeplink.DeepLinkRequest
import com.project.common.exceptions.APIException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@SpringBootTest
class DeepLinkServiceTest {

    @Autowired
    private lateinit var deepLinkService: DeepLinkService

    @Test
    fun `process deep link for wallet should return correct response`() {
        val request = DeepLinkRequest(deepLink = "nbkcapstone://wallet")
        val response = deepLinkService.processDeepLink(request)

        assertEquals("wallet", response.targetScreen)
        assertTrue(response.requiresAuth)
        assertEquals("Navigate to wallet screen", response.message)
    }

    @Test
    fun `process deep link for login should not require auth`() {
        val request = DeepLinkRequest(deepLink = "nbkcapstone://login")
        val response = deepLinkService.processDeepLink(request)

        assertEquals("login", response.targetScreen)
        assertFalse(response.requiresAuth)
        assertEquals("Navigate to login screen", response.message)
    }

    @Test
    fun `process deep link for promotion should extract promotionId`() {
        val request = DeepLinkRequest(deepLink = "nbkcapstone://promotion/123")
        val response = deepLinkService.processDeepLink(request)

        assertEquals("promotion", response.targetScreen)
        assertTrue(response.requiresAuth)
        assertEquals("123", response.parameters["promotionId"])
        assertEquals("Navigate to promotion details screen", response.message)
    }

    @Test
    fun `process deep link with invalid scheme should throw exception`() {
        val request = DeepLinkRequest(deepLink = "https://example.com/wallet")
        
        assertThrows<APIException> {
            deepLinkService.processDeepLink(request)
        }
    }

    @Test
    fun `process deep link for promotion without ID should throw exception`() {
        val request = DeepLinkRequest(deepLink = "nbkcapstone://promotion")
        
        assertThrows<APIException> {
            deepLinkService.processDeepLink(request)
        }
    }

    @Test
    fun `generate deep link for wallet should return correct URL`() {
        val request = DeepLinkRequest(targetScreen = "wallet")
        val response = deepLinkService.generateDeepLink(request)

        assertEquals("wallet", response.targetScreen)
        assertTrue(response.requiresAuth)
        assertEquals("nbkcapstone://wallet", response.deepLink)
        assertEquals("Generated deep link", response.message)
    }

    @Test
    fun `generate deep link for promotion should include promotionId`() {
        val request = DeepLinkRequest(
            targetScreen = "promotion",
            parameters = mapOf("promotionId" to "456")
        )
        val response = deepLinkService.generateDeepLink(request)

        assertEquals("promotion", response.targetScreen)
        assertTrue(response.requiresAuth)
        assertEquals("nbkcapstone://promotion/456", response.deepLink)
        assertEquals("456", response.parameters["promotionId"])
    }

    @Test
    fun `generate deep link for promotion without ID should throw exception`() {
        val request = DeepLinkRequest(targetScreen = "promotion")
        
        assertThrows<APIException> {
            deepLinkService.generateDeepLink(request)
        }
    }

    @Test
    fun `validate deep link for valid URL should return success`() {
        val request = DeepLinkRequest(deepLink = "nbkcapstone://wallet")
        val response = deepLinkService.validateDeepLink(request)

        assertEquals("wallet", response.targetScreen)
        assertTrue(response.requiresAuth)
        assertEquals("Deep link is valid", response.message)
    }

    @Test
    fun `validate deep link for invalid screen should throw exception`() {
        val request = DeepLinkRequest(deepLink = "nbkcapstone://invalid-screen")
        
        assertThrows<APIException> {
            deepLinkService.validateDeepLink(request)
        }
    }

    @Test
    fun `get supported screens should return all valid screens`() {
        val supportedScreens = deepLinkService.getSupportedScreens()
        
        assertTrue(supportedScreens.contains("login"))
        assertTrue(supportedScreens.contains("wallet"))
        assertTrue(supportedScreens.contains("promotion"))
        assertFalse(supportedScreens.contains("invalid-screen"))
    }

    @Test
    fun `requires authentication should return correct values`() {
        assertFalse(deepLinkService.requiresAuthentication("login"))
        assertFalse(deepLinkService.requiresAuthentication("signup"))
        assertTrue(deepLinkService.requiresAuthentication("wallet"))
        assertTrue(deepLinkService.requiresAuthentication("promotion"))
    }
} 