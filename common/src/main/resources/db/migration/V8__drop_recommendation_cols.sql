ALTER TABLE "recommendations"
    DROP COLUMN "category_id";

ALTER TABLE "recommendations"
    DROP COLUMN "promotion_id";

ALTER TABLE "recommendations"
    DROP COLUMN "partner_id";

ALTER TABLE "recommendations"
    RENAME COLUMN "product_id" TO "generic_id_ref";

