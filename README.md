![](/common/src/main/resources/assets/logo.png)

Welcome to **Klue** — your onboarding banking rewards platform blending secure accounts, XP tiers, partner perks, and smart, contextual notifications.

<!-- TOC -->
  * [Architecture](#architecture)
  * [Main Features](#main-features)
  * [Why Klue Works](#why-klue-works)
  * [User Journey](#user-journey)
  * [Core Endpoints](#core-endpoints)
    * [Auth Service](#auth-service)
    * [Banking Service](#banking-service)
    * [Recommendation Service](#recommendation-service)
    * [Notification Service](#notification-service)
  * [Tech Stack](#tech-stack)
<!-- TOC -->

---

## Architecture

A true microservices approach:

* **Auth Service** — Handles user registration, login, JWTs, token refresh, and validation.
* **Banking Service** — Manages accounts, KYC, transactions, transfers, purchases, and XP logic.
* **Recommendation Service** — Powers user favorites and personalized suggestions.
* **Notification Service** — Delivers timely nudges based on user location and behavior.

Services communicate over REST and keep clean boundaries using DTOs.

---

## Main Features

* Smooth onboarding with KYC
* Multi-account management
* Secure authentication and token validation
* XP earning, tier progression, and perks
* Favorite partners and categories
* Personalized product/card recommendations
* Calendar view of active promotions
* Geofence and BLE-triggered notifications

---

## Why Klue Works

Klue keeps your customers engaged and spending — driving loyalty and deeper partner collaboration. It turns ordinary spending into real value by applying your card’s perks, tracking partner promotions, and pushing timely notifications wherever you are.

---

## User Journey

1. Sign up and verify identity (KYC)
2. Open and manage accounts
3. Make purchases to earn XP and cashback
4. Level up through tiers to unlock bigger & better perks
5. Receive tailored notifications for promotions, stores, and recommended products

---

## Core Endpoints

### Auth Service

| Method | Endpoint                | Description                    |
|--------|-------------------------|--------------------------------|
| POST   | `/api/v1/auth/register` | Register new user & get JWT    |
| POST   | `/api/v1/auth/login`    | Login with username/password   |
| POST   | `/api/v1/auth/refresh`  | Refresh access token           |
| POST   | `/api/v1/auth/validate` | Validate token & get user info |

---

### Banking Service

| Method | Endpoint                                    | Description                                  |
|--------|---------------------------------------------|----------------------------------------------|
| POST   | `/api/v1/kyc`                               | Create or update your KYC                    |
| GET    | `/api/v1/kyc`                               | Get your KYC info                            |
| GET    | `/api/v1/accounts`                          | See all your active accounts                 |
| POST   | `/api/v1/accounts`                          | Create a new account                         |
| DELETE | `/api/v1/accounts/close/{accountNumber}`    | Close an account                             |
| GET    | `/api/v1/accounts/details`                  | Get account details by ID or number          |
| POST   | `/api/v1/accounts/transfer`                 | Transfer money between your accounts         |
| POST   | `/api/v1/accounts/purchase`                 | Make a purchase                              |
| GET    | `/api/v1/transactions/account`              | Get your transactions by account ID/number   |
| GET    | `/api/v1/xp`                                | See your XP stats                            |
| GET    | `/api/v1/xp/history`                        | Review your XP history                       |
| GET    | `/api/v1/xp/tiers`                          | Get all XP tiers                             |
| GET    | `/api/v1/xp/tiers/{id}`                     | Get XP tier by ID                            |
| GET    | `/api/v1/products`                          | Get all account product types                |
| GET    | `/api/v1/products/{productId}`              | Get details for one account product          |
| GET    | `/api/v1/products/perks/{perkId}`           | Get details for one perk                     |
| GET    | `/api/v1/products/{productId}/perks`        | Get all perks for an account product         |
| GET    | `/api/v1/categories`                        | Get all categories                           |
| GET    | `/api/v1/partners`                          | Get all business partners                    |
| GET    | `/api/v1/partners/{partnerId}`              | Get business partner by ID                   |
| GET    | `/api/v1/partners/by-category/{categoryId}` | Get partners by category                     |

---

### Recommendation Service

| Method | Endpoint                                            | Description                                      |
|--------|-----------------------------------------------------|--------------------------------------------------|
| GET    | `/api/v1/recommendations`                           | Get top recommended account products             |
| GET    | `/api/v1/fav/businesses`                            | Get all your favorite businesses                 |
| POST   | `/api/v1/fav/businesses`                            | Replace your favorite businesses                 |
| PUT    | `/api/v1/fav/businesses`                            | Add a business to favorites                      |
| DELETE | `/api/v1/fav/businesses`                            | Remove multiple businesses from favorites        |
| DELETE | `/api/v1/fav/businesses/remove`                     | Remove one business from favorites               |
| DELETE | `/api/v1/fav/businesses/clear`                      | Clear all favorite businesses                    |
| GET    | `/api/v1/fav/categories`                            | Get all your favorite categories                 |
| POST   | `/api/v1/fav/categories`                            | Replace your favorite categories                 |
| PUT    | `/api/v1/fav/categories`                            | Add a category to favorites                      |
| DELETE | `/api/v1/fav/categories`                            | Remove multiple categories from favorites        |
| DELETE | `/api/v1/fav/categories/remove`                     | Remove one category from favorites               |
| DELETE | `/api/v1/fav/categories/clear`                      | Clear all favorite categories                    |
| GET    | `/api/v1/promotions`                                | Get all promotions                               |
| GET    | `/api/v1/promotions/{id}`                           | Get promotion by ID                              |
| GET    | `/api/v1/promotions/business/{businessId}`          | Get promotions for a business                    |
| GET    | `/api/v1/promotions/business/{businessId}/active`   | Get active promotions for business               |
| GET    | `/api/v1/store-locations`                           | Get all store locations                          |
| GET    | `/api/v1/store-locations/details/{storeLocationId}` | Get store location details                       |
| POST   | `/api/v1/store-locations/near-me`                   | Find nearby stores                               |

---

### Notification Service

| Method | Endpoint                                         | Description                             |
|--------|--------------------------------------------------|-----------------------------------------|
| GET    | `/api/v1/notifications`                          | Get all your notifications              |
| GET    | `/api/v1/notifications/details/{notificationId}` | Get details for a specific notification |
| GET    | `/api/v1/notifications/search`                   | Look up notification sent to you        |

---

## Tech Stack

* **Language:** Kotlin + Spring Boot
* **Security:** Spring Security, JWT tokens
* **Database:** PostgreSQL (+ PostGIS for smart location data)
* **Pattern:** DTOs everywhere, no direct entity leaks
* **Communication:** REST endpoints, simple and clean

> Klue: know more, get more.
