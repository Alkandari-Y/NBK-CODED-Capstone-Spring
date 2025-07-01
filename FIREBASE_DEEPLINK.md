# Firebase Deep Link Integration - Promotion & Card Product Notifications

This document describes the Firebase Cloud Messaging (FCM) integration for sending promotion and card product deep link notifications in the NBK Capstone application.

## Overview

The notification microservice now supports sending promotion and card product deep link notifications via Firebase Cloud Messaging. When users receive these notifications, they can tap on them to open the app and navigate directly to the relevant details screen using deep links.

## Architecture

### Components

1. **DeepLinkNotificationService** - Handles sending promotion and card product deep link notifications via FCM
2. **DeepLinkNotificationController** - REST API endpoints for sending deep link notifications
3. **Updated NotificationServiceImp** - Enhanced existing notifications with deep link functionality

## Firebase Message Structure

### FCM Message with Promotion Deep Link Data

```json
{
  "token": "user_firebase_token",
  "data": {
    "title": "New Promotion Available",
    "body": "Check out the latest offers at Starbucks!",
    "deepLink": "nbkcapstone://promotion/123",
    "targetScreen": "promotion",
    "requiresAuth": "true",
    "parameters": "{promotionId=123}"
  },
  "notification": {
    "title": "New Promotion Available",
    "body": "Check out the latest offers at Starbucks!"
  }
}
```

### FCM Message with Card Product Deep Link Data

```json
{
  "token": "user_firebase_token",
  "data": {
    "title": "Card Recommendation",
    "body": "We found a perfect card for you!",
    "deepLink": "nbkcapstone://card-product/456",
    "targetScreen": "card-product",
    "requiresAuth": "true",
    "parameters": "{cardProductId=456}"
  },
  "notification": {
    "title": "Card Recommendation",
    "body": "We found a perfect card for you!"
  }
}
```

## API Endpoints

### Base URL

```
/api/v1/notifications/deeplink
```

### 1. Send Promotion Deep Link Notification

**POST** `/api/v1/notifications/deeplink/promotion`

Send a promotion-specific deep link notification to a single user.

**Request Body:**

```json
{
  "userId": 123,
  "promotionId": "456",
  "title": "New Promotion Available",
  "message": "Check out our latest offer!"
}
```

### 2. Send Card Product Deep Link Notification

**POST** `/api/v1/notifications/deeplink/card-product`

Send a card product-specific deep link notification to a single user.

**Request Body:**

```json
{
  "userId": 123,
  "cardProductId": "789",
  "title": "Card Recommendation",
  "message": "We found a perfect card for you!"
}
```

### 3. Send Bulk Promotion Deep Link Notifications

**POST** `/api/v1/notifications/deeplink/promotion/bulk`

Send promotion deep link notifications to multiple users.

**Request Body:**

```json
{
  "userIds": [123, 456, 789],
  "promotionId": "456",
  "title": "New Promotion Available",
  "message": "Check out our latest offer!"
}
```

### 4. Send Bulk Card Product Deep Link Notifications

**POST** `/api/v1/notifications/deeplink/card-product/bulk`

Send card product deep link notifications to multiple users.

**Request Body:**

```json
{
  "userIds": [123, 456, 789],
  "cardProductId": "789",
  "title": "Card Recommendation",
  "message": "We found a perfect card for you!"
}
```

## Automatic Deep Link Integration

### Existing Notifications Enhanced

The following existing notification types now automatically include deep links when relevant IDs are present:

1. **Geofence Notifications**

   - If promotionId exists: `nbkcapstone://promotion/{promotionId}`
   - If no promotionId: No deep link (regular notification)

2. **BLE Beacon Notifications**

   - If promotionId exists: `nbkcapstone://promotion/{promotionId}`
   - If no promotionId: No deep link (regular notification)

3. **Account Score Notifications**
   - If recommendationId exists: `nbkcapstone://card-product/{recommendationId}`
   - If no recommendationId: No deep link (regular notification)

## Mobile App Integration

### Handling Deep Link Notifications

The mobile app should handle FCM messages with deep link data:

```kotlin
// In your FCM message handler
override fun onMessageReceived(remoteMessage: RemoteMessage) {
    val data = remoteMessage.data

    // Extract deep link information
    val deepLink = data["deepLink"]
    val targetScreen = data["targetScreen"]
    val requiresAuth = data["requiresAuth"]?.toBoolean() ?: true
    val parameters = data["parameters"]

    if (deepLink != null) {
        // Process the deep link based on target screen
        when (targetScreen) {
            "promotion" -> handlePromotionDeepLink(deepLink, parameters)
            "card-product" -> handleCardProductDeepLink(deepLink, parameters)
            else -> handleGenericDeepLink(deepLink, targetScreen, parameters)
        }
    }
}

private fun handlePromotionDeepLink(deepLink: String, parameters: String?) {
    val promotionId = extractPromotionId(parameters)

    if (isUserLoggedIn()) {
        navigateToPromotionDetails(promotionId)
    } else {
        storeIntendedDestination(deepLink)
        navigateToLogin()
    }
}

private fun handleCardProductDeepLink(deepLink: String, parameters: String?) {
    val cardProductId = extractCardProductId(parameters)

    if (isUserLoggedIn()) {
        navigateToCardProductDetails(cardProductId)
    } else {
        storeIntendedDestination(deepLink)
        navigateToLogin()
    }
}
```

### Notification Tap Handling

When users tap on notifications, the app should extract and process the deep link:

```kotlin
// In your notification tap handler
override fun onNotificationTap(intent: Intent) {
    val deepLink = intent.getStringExtra("deepLink")
    val targetScreen = intent.getStringExtra("targetScreen")
    val parameters = intent.getStringExtra("parameters")

    if (deepLink != null) {
        when (targetScreen) {
            "promotion" -> processPromotionDeepLink(deepLink, parameters)
            "card-product" -> processCardProductDeepLink(deepLink, parameters)
            else -> processGenericDeepLink(deepLink, targetScreen, parameters)
        }
    }
}
```

## Deep Link Mapping

### Supported Deep Links

| Notification Type | Deep Link                         | Target Screen | Parameters    |
| ----------------- | --------------------------------- | ------------- | ------------- |
| Promotion         | `nbkcapstone://promotion/{id}`    | promotion     | promotionId   |
| Card Product      | `nbkcapstone://card-product/{id}` | card-product  | cardProductId |

## Error Handling

### Common Error Scenarios

1. **User Device Not Found**

   - Logs warning and returns early
   - No exception thrown

2. **Firebase Token Invalid**

   - Logs error and throws exception
   - Should trigger token refresh

3. **Invalid ID Format**

   - Validated before sending
   - Throws APIException for invalid formats

### Error Response Examples

```json
{
  "error": "No device found for user 123",
  "timestamp": "2024-01-15T10:30:00Z"
}
```

```json
{
  "error": "Invalid promotion ID format",
  "timestamp": "2024-01-15T10:30:00Z"
}
```

## Testing

### Test Cases

1. **Valid Deep Link Notifications**

   - Send promotion notifications with valid promotionId
   - Send card product notifications with valid cardProductId
   - Send bulk notifications to multiple users

2. **Authentication Requirements**

   - Test notifications that require authentication
   - Verify deep link navigation works correctly

3. **Error Scenarios**
   - Test with non-existent user
   - Test with invalid Firebase token
   - Test with malformed IDs

### Example Test Requests

```bash
# Send promotion notification
curl -X POST http://localhost:8081/api/v1/notifications/deeplink/promotion \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 123,
    "promotionId": "456",
    "title": "New Promotion",
    "message": "Check out our latest offer!"
  }'

# Send card product notification
curl -X POST http://localhost:8081/api/v1/notifications/deeplink/card-product \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 123,
    "cardProductId": "789",
    "title": "Card Recommendation",
    "message": "We found a perfect card for you!"
  }'

# Send bulk promotion notification
curl -X POST http://localhost:8081/api/v1/notifications/deeplink/promotion/bulk \
  -H "Content-Type: application/json" \
  -d '{
    "userIds": [123, 456],
    "promotionId": "456",
    "title": "New Promotion",
    "message": "Check out our latest offer!"
  }'

# Send bulk card product notification
curl -X POST http://localhost:8081/api/v1/notifications/deeplink/card-product/bulk \
  -H "Content-Type: application/json" \
  -d '{
    "userIds": [123, 456],
    "cardProductId": "789",
    "title": "Card Recommendation",
    "message": "We found a perfect card for you!"
  }'
```

## Security Considerations

1. **Authentication Validation**

   - Deep link endpoints require authentication
   - User ID validation before sending notifications

2. **Input Validation**

   - All ID formats are validated
   - Parameters are sanitized

3. **Rate Limiting**

   - Consider implementing rate limiting for notification endpoints
   - Prevent spam notifications

4. **Logging**
   - All notification attempts are logged
   - Failed notifications are logged with error details

## Performance Considerations

1. **Bulk Notifications**

   - Use bulk endpoint for multiple users
   - Process notifications in batches

2. **Firebase Token Management**

   - Implement token refresh mechanism
   - Remove invalid tokens from database

3. **Error Handling**
   - Don't retry failed notifications immediately
   - Implement exponential backoff for retries

## Future Enhancements

1. **Analytics Integration**

   - Track notification open rates
   - Measure deep link conversion rates

2. **Personalization**

   - Send personalized notifications based on user preferences
   - Dynamic content generation

3. **A/B Testing**

   - Test different notification strategies
   - Optimize notification content

4. **Scheduled Notifications**
   - Send notifications at optimal times
   - Timezone-aware scheduling
