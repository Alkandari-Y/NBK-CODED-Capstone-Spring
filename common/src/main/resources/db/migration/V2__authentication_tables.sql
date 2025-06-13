DROP TABLE IF EXISTS "user_roles";
DROP TABLE IF EXISTS "roles";
DROP TABLE IF EXISTS "users";


CREATE TABLE "users" (
                         "id"           SERIAL PRIMARY KEY ,
                         "civil_id"     VARCHAR(255)        NOT NULL UNIQUE,
                         "username"     VARCHAR(255)        NOT NULL UNIQUE,
                         "email"        VARCHAR(255)        NOT NULL UNIQUE,
                         "password"     VARCHAR(255)        NOT NULL,
                         "created_at"   DATE                NOT NULL,
                         "updated_at"   DATE                NOT NULL,
                         "is_active"    BOOLEAN             NOT NULL
);

CREATE TABLE "roles" (
                         "id"           SERIAL PRIMARY KEY ,
                         "name"         VARCHAR(255)        NOT NULL UNIQUE
);

CREATE TABLE "user_roles" (
                              "id"            SERIAL PRIMARY KEY ,
                              "user_id"       INT                 NOT NULL,
                              "role_id"       INT                 NOT NULL,
                              CONSTRAINT "fk_user"
                                  FOREIGN KEY ("user_id")
                                      REFERENCES "users"("id") ON DELETE CASCADE,
                              CONSTRAINT "fk_role"
                                  FOREIGN KEY ("role_id")
                                      REFERENCES "roles"("id") ON DELETE CASCADE
);
