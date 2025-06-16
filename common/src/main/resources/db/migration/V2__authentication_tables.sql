CREATE TABLE IF NOT EXISTS "users" (
     "id"           SERIAL PRIMARY KEY ,
     "username"     VARCHAR(255)        NOT NULL UNIQUE,
     "email"        VARCHAR(255)        NOT NULL UNIQUE,
     "password"     VARCHAR(255)        NOT NULL,
     "created_at"   TIMESTAMP                NOT NULL,
     "updated_at"   TIMESTAMP                NOT NULL,
     "is_active"    BOOLEAN             NOT NULL
);

CREATE TABLE IF NOT EXISTS "roles" (
     "id"           SERIAL PRIMARY KEY ,
     "name"         VARCHAR(255)        NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS "user_roles" (
      "id"            SERIAL PRIMARY KEY ,
      "user_id"       INT                 NOT NULL,
      "role_id"       INT                 NOT NULL,
      CONSTRAINT "fk_user"
          FOREIGN KEY ("user_id")
              REFERENCES "users"("id") ON DELETE CASCADE,
      CONSTRAINT "fk_role"
          FOREIGN KEY ("role_id")
              REFERENCES "roles"("id") ON DELETE CASCADE,
    CONSTRAINT "unique_user_role"
        UNIQUE ("user_id", "role_id")
);
