ALTER TABLE "account_products"
    ALTER COLUMN "min_salary" TYPE DECIMAL(9, 3);


ALTER TABLE "accounts"
    DROP COLUMN "name";