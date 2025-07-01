# Deep Link Implementation - Backend

This document describes the backend implementation for handling deep links in the NBK Capstone application.

## Overview

The backend provides REST API endpoints to process, validate, and generate deep links that use the `nbkcapstone://` scheme. This allows the mobile app to handle deep link navigation and share links with users.

## API Endpoints

### Base URL

```
/api/v1/deeplink
```

### 1. Process Deep Link

**POST** `/api/v1/deeplink/process`

Processes a deep link and returns navigation information.

**Request Body:**

```json
{
  "deepLink": "nbkcapstone://wallet"
}
```

**Response:**

```json
{
  "targetScreen": "wallet",
  "requiresAuth": true,
  "parameters": {},
  "message": "Navigate to wallet screen"
}
```

### 2. Generate Deep Link

**POST** `/api/v1/deeplink/generate`

Generates a deep link URL for sharing.

**Request Body:**

```json
{
  "targetScreen": "promotion",
  "parameters": {
    "promotionId": "123"
  }
}
```

**Response:**

```json
{
  "targetScreen": "promotion",
  "requiresAuth": true,
  "parameters": {
    "promotionId": "123"
  },
  "message": "Generated deep link",
  "deepLink": "nbkcapstone://promotion/123"
}
```

### 3. Validate Deep Link

**POST** `/api/v1/deeplink/validate`

Validates the format of a deep link.

**Request Body:**

```json
{
  "deepLink": "nbkcapstone://invalid-screen"
}
```

**Response:**

```json
{
  "error": "Invalid target screen: invalid-screen"
}
```

## Supported Deep Links

### Public Screens (No Authentication Required)

- `nbkcapstone://login` - Login screen
- `nbkcapstone://signup` - Sign up screen

### Protected Screens (Authentication Required)

- `nbkcapstone://home` - Home screen
- `nbkcapstone://wallet` - Wallet screen
- `nbkcapstone://transfer` - Transfer screen
- `nbkcapstone://calendar` - Calendar screen
- `nbkcapstone://recommendations` - Recommendations screen
- `nbkcapstone://profile` - Profile screen
- `nbkcapstone://xp` - XP history screen
- `nbkcapstone://notifications` - Notifications screen
- `nbkcapstone://promotion/{promotionId}` - Promotion details screen

## Data Transfer Objects

### DeepLinkRequest

```kotlin
data class DeepLinkRequest(
    val deepLink: String? = null,        // For processing/validating
    val targetScreen: String? = null,    // For generating
    val parameters: Map<String, String>? = null  // Additional parameters
)
```

### DeepLinkResponse

```kotlin
data class DeepLinkResponse(
    val targetScreen: String,            // Target screen to navigate to
    val requiresAuth: Boolean,           // Whether authentication is required
    val parameters: Map<String, String>, // Extracted parameters
    val message: String,                 // Human-readable message
    val deepLink: String? = null         // Generated deep link (for generate endpoint)
)
```

## Implementation Details

### Architecture

- **Controller**: `DeepLinkController` - Handles HTTP requests
- **Service**: `DeepLinkService` - Business logic for deep link processing
- **DTOs**: Request/Response data transfer objects in common module

### Security

- Deep link endpoints are publicly accessible (no authentication required)
- This allows the mobile app to process deep links before user authentication
- The `requiresAuth` field in the response indicates whether the target screen needs authentication

### Error Handling

- Invalid deep link schemes throw `APIException`
- Missing required parameters (e.g., promotionId) throw `APIException`
- Invalid target screens throw `APIException`

## Integration with Mobile App

### Frontend Integration

The mobile app can use these endpoints to:

1. **Process incoming deep links**: When a user clicks a deep link, the app can call `/process` to get navigation information
2. **Generate shareable links**: Use `/generate` to create deep links for sharing promotions, etc.
3. **Validate links**: Use `/validate` to check if a deep link is properly formatted

### Example Mobile App Flow

```kotlin
// 1. User clicks deep link: nbkcapstone://promotion/123
val deepLink = "nbkcapstone://promotion/123"

// 2. App calls backend to process the link
val response = apiService.processDeepLink(DeepLinkRequest(deepLink = deepLink))

// 3. App checks if authentication is required
if (response.requiresAuth && !isUserLoggedIn()) {
    // Store intended destination and navigate to login
    storeIntendedDestination(deepLink)
    navigateToLogin()
} else {
    // Navigate directly to target screen
    navigateToScreen(response.targetScreen, response.parameters)
}
```

## Testing

### Test Cases

1. **Valid deep links**: All supported screens should return appropriate responses
2. **Invalid schemes**: Should throw error for non-nbkcapstone schemes
3. **Invalid screens**: Should throw error for unsupported screens
4. **Missing parameters**: Should throw error for promotion links without promotionId
5. **Authentication requirements**: Should correctly identify which screens require auth

### Example Test Requests

```bash
# Process valid deep link
curl -X POST http://localhost:8080/api/v1/deeplink/process \
  -H "Content-Type: application/json" \
  -d '{"deepLink": "nbkcapstone://wallet"}'

# Generate deep link
curl -X POST http://localhost:8080/api/v1/deeplink/generate \
  -H "Content-Type: application/json" \
  -d '{"targetScreen": "promotion", "parameters": {"promotionId": "123"}}'

# Validate deep link
curl -X POST http://localhost:8080/api/v1/deeplink/validate \
  -H "Content-Type: application/json" \
  -d '{"deepLink": "nbkcapstone://invalid-screen"}'
```

## Future Enhancements

1. **Analytics**: Track deep link usage and conversion rates
2. **Dynamic parameters**: Support for additional parameters beyond promotionId
3. **Link expiration**: Add expiration dates to generated deep links
4. **User-specific links**: Generate personalized deep links for specific users
5. **Campaign tracking**: Add campaign parameters for marketing purposes

## Security Considerations

1. **Input validation**: All deep link inputs are validated for proper format
2. **Parameter sanitization**: Parameters are extracted and validated
3. **Rate limiting**: Consider implementing rate limiting for deep link endpoints
4. **Logging**: Deep link processing should be logged for security monitoring
