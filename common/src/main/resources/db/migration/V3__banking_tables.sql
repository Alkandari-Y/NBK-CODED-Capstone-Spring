DROP TABLE IF EXISTS "account_ownerships";
DROP TABLE IF EXISTS "transactions";
DROP TABLE IF EXISTS "accounts";
DROP TABLE IF EXISTS "profiles";
DROP TABLE IF EXISTS "categories";

CREATE TABLE "categories"
(
    "id"   SERIAL PRIMARY KEY,
    "name" VARCHAR(255) NOT NULL
);

CREATE TABLE "kycs"
(
    "id"            SERIAL PRIMARY KEY,
    "user_id"       INT           NOT NULL UNIQUE,
    "first_name"    VARCHAR(255)  NOT NULL,
    "last_name"     VARCHAR(255)  NOT NULL,
    "date_of_birth" DATE          NOT NULL,
    "nationality"   VARCHAR(255)  NOT NULL,
    "salary"        DECIMAL(9, 3) NOT NULL
);

CREATE TABLE "accounts"
(
    "id"                    SERIAL PRIMARY KEY,
    "name"                  VARCHAR(255)  NOT NULL,
    "balance"               DECIMAL(9, 3) NOT NULL,
    "is_active"             BOOLEAN       NOT NULL,
    "account_number"        VARCHAR(255)  NOT NULL UNIQUE,
    "owner_id"              INT           NOT NULL,
    "account_type"          INT           NOT NULL
);

CREATE TABLE "transactions"
(
    "id"                  SERIAL PRIMARY KEY,
    "source_account"      INT           NOT NULL,
    "destination_account" INT           NOT NULL,
    "amount"              DECIMAL(9, 3) NOT NULL,
    "created_at"          TIMESTAMP     NOT NULL,
    "category_id"         INT           NOT NULL,
    "type"                INT           NOT NULL,
    CONSTRAINT "fk_transaction_source"
        FOREIGN KEY ("source_account")
            REFERENCES "accounts" ("id") ON DELETE CASCADE,
    CONSTRAINT "fk_transaction_destination"
        FOREIGN KEY ("destination_account")
            REFERENCES "accounts" ("id") ON DELETE CASCADE,
    CONSTRAINT "fk_transaction_category"
        FOREIGN KEY ("category_id")
            REFERENCES "categories" ("id")
);


INSERT INTO "categories" ( "name")
VALUES ( 'personal'),
       ('retail'),
       ( 'manufacturing'),
       ( 'healthcare'),
       ( 'financial services'),
       ( 'real estate'),
       ( 'technology'),
       ( 'hospitality'),
       ( 'education'),
       ( 'logistics'),
       ( 'construction'),
       ( 'agriculture'),
       ( 'automotive'),
       ( 'consulting'),
       ( 'wholesale'),
       ( 'energy');