CREATE TABLE IF NOT EXISTS "notifications"(
   "id" SERIAL PRIMARY KEY,
   "user_id" BIGINT NOT NULL,
   "message" VARCHAR(255) NOT NULL,
    "delivery_type" INT NOT NULL,
    "created_at" TIMESTAMP NOT NULL,
    "delivered" BOOLEAN NOT NULL,
    "partner_id" BIGINT NULL,
    "event_id" BIGINT NULL,
    "recommendation_id" BIGINT NULL,
    "promotion_id" BIGINT NULL,
    "trigger_type" INT
);

CREATE TABLE IF NOT EXISTS "notification_settings"(
   "id" SERIAL PRIMARY KEY,
   "delivery_type" INT NOT NULL,
   "user_id" BIGINT NOT NULL,
   "reward_type" INT NOT NULL,
   "active" BOOLEAN NOT NULL
);

CREATE TABLE IF NOT EXISTS "user_devices"(
    "id" SERIAL PRIMARY KEY,
    "firebase_token" VARCHAR(255) NOT NULL UNIQUE,
    "user_id" BIGINT NOT NULL
);

