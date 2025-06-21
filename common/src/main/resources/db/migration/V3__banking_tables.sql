CREATE TABLE IF NOT EXISTS "categories"
(
    "id"            SERIAL PRIMARY KEY,
    "name"          VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS "kycs"
(
    "id"            SERIAL PRIMARY KEY,
    "user_id" BIGINT NOT NULL,
    "first_name" VARCHAR(255) NOT NULL,
    "last_name" VARCHAR(255) NOT NULL,
    "date_of_birth" DATE NOT NULL,
    "nationality" VARCHAR(255) NOT NULL,
    "salary" DECIMAL(9, 3) NOT NULL,
    "civil_id" VARCHAR(255) NOT NULL,
    "mobile_number" VARCHAR(255) NOT NULL,
    "is_verified" BOOLEAN NOT NULL
);

CREATE TABLE IF NOT EXISTS  "xp_tiers"(
    "id" SERIAL PRIMARY KEY,
    "min_xp" BIGINT NOT NULL,
    "max_xp" BIGINT NOT NULL,
    "name" VARCHAR(255) NOT NULL,
    "xp_perk_multiplier" BIGINT NOT NULL,
    "xp_per_notification" BIGINT NOT NULL, -- UPDATED!!! (there was a typo)
    "xp_per_promotion" BIGINT NOT NULL, -- UPDATED!!!
    "perk_amount_percentage" BIGINT NOT NULL
);

CREATE TABLE IF NOT EXISTS "user_xp"(
    "id" SERIAL PRIMARY KEY,
    "user_id" BIGINT NOT NULL,
    "amount" BIGINT NOT NULL
);

CREATE TABLE IF NOT EXISTS "account_products"(
    "id"            SERIAL PRIMARY KEY,
    "name"          VARCHAR(255) UNIQUE NOT NULL,
    "type"          INT NOT NULL,
    "interest_rate" DECIMAL(9, 3) NULL,
    "min_balance_required" DECIMAL(9, 3) NULL,
    "credit_limit"  DECIMAL(9, 3) NULL,
    "annual_fee"    DECIMAL(9, 3) NULL,
    "min_salary"    BIGINT NULL,
    "image"         VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS "accounts"
(
    "id"                    SERIAL PRIMARY KEY,
    "name"                  VARCHAR(255)  NOT NULL,
    "balance"               DECIMAL(9, 3) NOT NULL,
    "is_active"             BOOLEAN       NOT NULL,
    "account_number"        VARCHAR(255)  NOT NULL UNIQUE,
    "owner_id"              INT           NOT NULL,
    "owner_type"            INT           NOT NULL,
    "account_product_id"    BIGINT NOT NULL,
    "account_type"          INT           NOT NULL,
    CONSTRAINT "accounts_account_product_id_foreign"
    FOREIGN KEY ("account_product_id")
    REFERENCES "account_products" ("id") ON DELETE CASCADE
    );

CREATE TABLE IF NOT EXISTS "business_partners"(
    "id"            SERIAL PRIMARY KEY,
    "name"          VARCHAR(255) UNIQUE NOT NULL,
    "admin_user"    BIGINT NOT NULL,
    "account_id"    BIGINT NOT NULL,
    "logo_url"      VARCHAR(255) NOT NULL,
    "category_id"   BIGINT NOT NULL,
    CONSTRAINT "business_partners_account_id_foreign"
        FOREIGN KEY ("account_id")
            REFERENCES "accounts" ("id") ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS "perks"(
    "id" SERIAL PRIMARY KEY,
    "account_product_id" BIGINT NOT NULL,
    "type" INT NOT NULL,
    "min_payment" DECIMAL(9, 3) NOT NULL,
    "rewards_xp" BIGINT NOT NULL,
    "perk_amount" DECIMAL(9, 3) NOT NULL,
    "is_tier_based" BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT "account_products_id_foreign"
        FOREIGN KEY("account_product_id")
            REFERENCES "account_products"("id")
);

CREATE TABLE IF NOT EXISTS "perk_categories"(
    "id" SERIAL PRIMARY KEY,
    "category_id" BIGINT NOT NULL,
    "perk_id" BIGINT NOT NULL,
    CONSTRAINT "perk_categories_perk_id_foreign"
        FOREIGN KEY ("perk_id")
            REFERENCES "perks" ("id") ON DELETE CASCADE,
    CONSTRAINT "perk_categories_id_foreign"
        FOREIGN KEY ("category_id")
            REFERENCES "categories" ("id") ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS "transactions"
(
    "id"                  SERIAL PRIMARY KEY,
    "source_account"      VARCHAR(255)           NOT NULL,
    "destination_account" VARCHAR(255)           NOT NULL,
    "amount"              DECIMAL(9, 3) NOT NULL,
    "created_at"          TIMESTAMP     NOT NULL,
    "category_id"         INT           NOT NULL,
    "transaction_type"                INT           NOT NULL,
    CONSTRAINT "transaction_category_id_foreign"
        FOREIGN KEY ("category_id")
            REFERENCES "categories" ("id"),
    CONSTRAINT "transaction_source_account_foreign"
        FOREIGN KEY("source_account")
            REFERENCES "accounts"("account_number"),
    CONSTRAINT "transaction_destination_account_foreign"
        FOREIGN KEY("destination_account")
            REFERENCES "accounts"("account_number")
);

CREATE TABLE IF NOT EXISTS "xp_history"(
     "id" SERIAL PRIMARY KEY,
     "amount" BIGINT NOT NULL,
     "gain_method" INT NOT NULL,
     "transaction_id" BIGINT NULL,
     "category_id" BIGINT NOT NULL,
     "recommendation_id" BIGINT NULL,
     "promotion_id" BIGINT NULL,
     "xp_tier_id" BIGINT NOT NULL,
     "user_xp_id" BIGINT NULL,
     "account_id" BIGINT NOT NULL,
     "account_product_id" BIGINT NOT NULL,
     CONSTRAINT "xp_history_category_id_foreign"
         FOREIGN KEY ("category_id")
             REFERENCES "categories" ("id"),
     CONSTRAINT "xp_history_xp_tier_id_foreign"
         FOREIGN KEY("xp_tier_id")
             REFERENCES "xp_tiers"("id"),
     CONSTRAINT "xp_history_account_product_id_foreign"
         FOREIGN KEY("account_product_id")
             REFERENCES "account_products"("id"),
     CONSTRAINT "xp_history_transaction_id_foreign"
         FOREIGN KEY("transaction_id")
             REFERENCES "transactions"("id"),
     CONSTRAINT "xp_history_account_id_foreign"
        FOREIGN KEY("account_id")
            REFERENCES "accounts"("id"),
     CONSTRAINT "xp_history_user_xp_id_foreign"
         FOREIGN KEY("user_xp_id")
             REFERENCES "user_xp"("id")
);
