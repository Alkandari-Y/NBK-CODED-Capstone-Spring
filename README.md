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

| Method | Endpoint                | Description                  |
|--------|-------------------------|------------------------------|
| POST   | `/api/v1/auth/register` | Register new user & get JWT  |
| POST   | `/api/v1/auth/login`    | Login with username/password |

### Banking Service

| Method | Endpoint                                 | What It Does                          |
|--------|------------------------------------------|---------------------------------------|
| GET    | `/api/v1/accounts`                       | See all your active accounts          |
| POST   | `/api/v1/accounts`                       | Open a new account                    |
| POST   | `/api/v1/accounts/onboarding/create`     | Onboard and create first account      |
| POST   | `/api/v1/accounts/transfer`              | Transfer money between your accounts  |
| POST   | `/api/v1/accounts/purchase`              | Make a purchase & earn XP             |
| DELETE | `/api/v1/accounts/close/{accountNumber}` | Close an account                      |
| GET    | `/api/v1/transactions/account`           | Get transactions by account ID/number |
| POST   | `/api/v1/kyc`                            | Create or update KYC                  |
| GET    | `/api/v1/kyc`                            | Fetch your KYC info                   |
| GET    | `/api/v1/xp`                             | See your XP stats                     |
| GET    | `/api/v1/xp/history`                     | Review your XP history                |


### Recommendation Service

| Method | Endpoint                 | What It Does                     |
|--------|--------------------------|----------------------------------|
| GET    | `/api/v1/fav/businesses` | Get all your favorite partners   |
| PUT    | `/api/v1/fav/businesses` | Add a favorite partner           |
| GET    | `/api/v1/fav/categories` | Get all your favorite categories |
| PUT    | `/api/v1/fav/categories` | Add a favorite category          |

### Notification Service

| Method | Endpoint                                         | What It Does                            |
|--------|--------------------------------------------------|-----------------------------------------|
| GET    | `/api/v1/notifications`                          | See all your notifications              |
| GET    | `/api/v1/notifications/details/{notificationId}` | Get details for a specific notification |
| GET    | `/api/v1/notifications/search`                   | Look up notifications by user & partner |

---

## Tech Stack

* **Language:** Kotlin + Spring Boot
* **Security:** Spring Security, JWT tokens
* **Database:** PostgreSQL (+ PostGIS for smart location data)
* **Pattern:** DTOs everywhere, no direct entity leaks
* **Communication:** REST endpoints, simple and clean

> Klue: know more, get more.