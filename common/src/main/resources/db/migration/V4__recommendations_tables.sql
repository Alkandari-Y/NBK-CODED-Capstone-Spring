CREATE TABLE IF NOT EXISTS "recommendations"(
     "id"                   SERIAL PRIMARY KEY,
     "category_id" BIGINT NOT NULL,
     "promotion_id" BIGINT NULL, -- UPDATED!!!
     "user_id" BIGINT NOT NULL,
     "partner_id" BIGINT NULL,
     "rec_type" INT NOT NULL,
     "product_id" BIGINT NULL,
     "created_at" TIMESTAMP
);


CREATE TABLE IF NOT EXISTS "fav_businesses"(
     "id"                   SERIAL PRIMARY KEY,
     "user_id"              BIGINT NOT NULL,
     "partner_id"           BIGINT NOT NULL,
     CONSTRAINT "unique_fav_business"
     UNIQUE ("user_id", "partner_id")
);

CREATE TABLE IF NOT EXISTS "fav_categories"(
     "id"                   SERIAL PRIMARY KEY,
     "user_id"              BIGINT NOT NULL,
     "category_id"          BIGINT NOT NULL,
     "created_at" TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
     CONSTRAINT "unique_fav_categories"
        UNIQUE ("user_id", "category_id")
);

CREATE TABLE IF NOT EXISTS "promotion_categories"(
   "id"                     SERIAL PRIMARY KEY,
   "category_id"            BIGINT NOT NULL,
   "promotion_id"               BIGINT NOT NULL
);

CREATE TABLE IF NOT EXISTS "promotions"( -- UPDATED!!
    "id" SERIAL PRIMARY KEY,
    "name" VARCHAR(255) NOT NULL,
    "business_partner_id" BIGINT NOT NULL,
    "type" BIGINT NOT NULL,
    "start_date" DATE,
    "end_date" DATE,
    "description" VARCHAR(255) NOT NULL,
    "store_id" BIGINT NULL
);


CREATE TABLE IF NOT EXISTS "seasonal_events"(
    "id"                    SERIAL PRIMARY KEY,
    "name"                  VARCHAR(255) NOT NULL,
    "description"           TEXT NOT NULL,
    "start_date"            DATE NOT NULL,
    "end_date"              DATE NOT NULL
);

CREATE TABLE IF NOT EXISTS "participating_partners"(
    "id"                   SERIAL PRIMARY KEY,
    "partner_id"           BIGINT NOT NULL,
    "seasonal_event_id"    BIGINT NOT NULL,
    CONSTRAINT "participating_partners_seasonal_event_id_foreign"
        FOREIGN KEY("seasonal_event_id")
            REFERENCES "seasonal_events"("id")
);

CREATE TABLE IF NOT EXISTS "store_locations"(
   "id" SERIAL PRIMARY KEY,
   "partner_id" BIGINT NOT NULL,
   longitude DECIMAL(10, 8) NOT NULL,
   latitude DECIMAL(11, 8) NOT NULL,
    "google_map_url" VARCHAR(255) NOT NULL,
    "country" VARCHAR(255) NOT NULL,
    "address_line_1" VARCHAR(255) NOT NULL,
    "address_line_2" VARCHAR(255) NULL,
    "opens_at" TIME(0) WITHOUT TIME ZONE NOT NULL,
    "closes_at" TIME(0) WITHOUT TIME ZONE NOT NULL,
    "beacon_id" BIGINT NOT NULL,
   location GEOGRAPHY(POINT, 4326) GENERATED ALWAYS AS (
       ST_SetSRID(ST_MakePoint(longitude, latitude), 4326)
       ) STORED
);

CREATE INDEX idx_store_locations_location
    ON store_locations USING GIST(location);

CREATE TABLE IF NOT EXISTS "account_scores"(
    "id" SERIAL PRIMARY KEY,
    "account_id" BIGINT NOT NULL,
    "account_score_rating" DECIMAL(9, 3) NOT NULL,
    "user_id" BIGINT NOT NULL
);

CREATE TABLE IF NOT EXISTS "category_scores"(
     "id" SERIAL PRIMARY KEY,
     "user_id" BIGINT NOT NULL,
     "frequency" BIGINT NOT NULL,
     "category_id" BIGINT NOT NULL,
     CONSTRAINT "unique_user_categories"
         UNIQUE ("user_id", "category_id")
);
