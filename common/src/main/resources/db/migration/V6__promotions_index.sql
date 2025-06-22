CREATE INDEX idx_promotions_partner ON promotions(business_partner_id);
CREATE INDEX idx_promotions_dates ON promotions(start_date, end_date);
