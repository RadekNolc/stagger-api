DROP TABLE IF EXISTS "user";
CREATE TABLE IF NOT EXISTS "user" (
    "id" BIGINT NOT NULL AUTO_INCREMENT,
    "username" VARCHAR(24) NOT NULL,
    "password" VARCHAR(250) NOT NULL,
    "email_address" VARCHAR(50) NOT NULL,
    "phone_number" VARCHAR(20),
    "language" ENUM('EN','CS') NOT NULL DEFAULT 'EN',
    "is_active" TINYINT NOT NULL DEFAULT 1,
    "created_at" TIMESTAMP NOT NULL,
    "created_by" VARCHAR(24) NOT NULL DEFAULT '',
    "updated_at" TIMESTAMP,
    "updated_by" VARCHAR(24),
    PRIMARY KEY ("id")
);

DROP TABLE IF EXISTS "role";
CREATE TABLE IF NOT EXISTS "role" (
    "id" BIGINT NOT NULL AUTO_INCREMENT,
    "name" ENUM('ADMIN','MODERATOR','USER') NOT NULL DEFAULT 'USER',
    "created_at" TIMESTAMP NOT NULL,
    "created_by" VARCHAR(24) NOT NULL DEFAULT '',
    "updated_at" TIMESTAMP,
    "updated_by" VARCHAR(24),
    PRIMARY KEY ("id")
);

DROP TABLE IF EXISTS "roles_users_map";
CREATE TABLE IF NOT EXISTS "roles_users_map" (
    "user_id" BIGINT NOT NULL,
    "role_id" BIGINT NOT NULL,
    "id" BIGINT NOT NULL AUTO_INCREMENT,
    PRIMARY KEY ("id")
);

DROP TABLE IF EXISTS "university";
CREATE TABLE IF NOT EXISTS "university" (
    "id" BIGINT NOT NULL AUTO_INCREMENT,
    "name" VARCHAR(50) NOT NULL DEFAULT '',
    "created_at" TIMESTAMP NOT NULL,
    "created_by" VARCHAR(24) NOT NULL DEFAULT '',
    "updated_at" TIMESTAMP,
    "updated_by" VARCHAR(24),
    PRIMARY KEY ("id")
);

DROP TABLE IF EXISTS "subject";
CREATE TABLE IF NOT EXISTS "subject" (
    "id" BIGINT NOT NULL AUTO_INCREMENT,
    "university_id" BIGINT NOT NULL,
    "department" VARCHAR(15) NOT NULL DEFAULT '',
    "name" VARCHAR(15) NOT NULL DEFAULT '',
    PRIMARY KEY ("id")
);

DROP TABLE IF EXISTS "universities_users_map";
CREATE TABLE IF NOT EXISTS "universities_users_map" (
     "id" BIGINT NOT NULL AUTO_INCREMENT,
     "user_id" BIGINT NOT NULL,
     "university_id" BIGINT NOT NULL,
     PRIMARY KEY ("id")
);