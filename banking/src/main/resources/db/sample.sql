INSERT INTO public.roles (name)
VALUES ('ROLE_USER'),
       ('ROLE_ADMIN'),
       ('ROLE_DEVELOPER');


-- Insert users with admin first
INSERT INTO "users" ("civil_id", "username", "email", "password", "created_at", "updated_at", "is_active")
VALUES ( '111111111111', 'admin', 'admin@gmail.com', '$2a$10$KzdrH.oslwtuaKvF4Q/X1OZFawsndKNhJI0kC8rdjD/LdbSEXG9yK',
         '2025-05-11', '2025-05-11', true),
       ( '222222222222', 'testuser', 'testuser@snddev.com',
         '$2a$10$LZseq/6Q2MMKQHsqwLdVzO6EBBx.1K2CEcMUmA3LZOlpeG0p2kktu', '2025-05-11', '2025-05-11', true),


-- Insert all users with role 1 and give admin role 2 as well
INSERT INTO "user_roles" ("user_id", "role_id")
VALUES (1, 1), -- admin with role 1
       (1, 2), -- admin with role 2
       (1, 3), -- admin with role 3
       (2, 1); -- testuser with role 1


INSERT INTO "kycs" ("user_id",
                    "first_name",
                    "last_name",
                    "date_of_birth",
                    "nationality",
                    "salary")
VALUES (1, 'Django', 'SuperUser', '1994-01-01', 'Kuwaiti', 2000),
       (2, 'Test', 'User', '1994-01-01', 'Kuwaiti', 2000);


INSERT INTO "accounts" ("name", "balance", "is_active", "account_number", "owner_id", "owner_type")
VALUES
--     Client accounts with 0
( 'admin savings', 20000.000, true, '11111111', 1, 0),
( 'testuser savings', 20000.000, true, '22222222', 2, 0);
