/* USER TABLE (password = username) */
INSERT INTO "user" ("id", "username", "password", "email_address", "phone_number", "is_active", "created_at", "created_by", "updated_at", "updated_by")
VALUES (1, 'admin', '$2a$12$V/t1fRb1qvpOxGBoEHhDMOCgEpK7FBtLl0q9omX2iMbLjpfxI0c0W', 'admin@stagger.cz', '123456789', 1, '2023-04-16 00:13:47', 'admin', NULL, NULL);
INSERT INTO "user" ("id", "username", "password", "email_address", "phone_number", "is_active", "created_at", "created_by", "updated_at", "updated_by")
VALUES (2, 'user', '$2a$12$kOXYV3zxUWFeJHXueBvXremslfkq.e9Zqfr5Jdm4vD12SfZ9nZDwS', 'user@stagger.cz', '123456789', 1, '2023-04-16 00:13:47', 'user', NULL, NULL);
INSERT INTO "user" ("id", "username", "password", "email_address", "phone_number", "is_active", "created_at", "created_by", "updated_at", "updated_by")
VALUES (3, 'inactive', '$2a$12$j4Zm41Uy9VYNXisppiCWSOTGS9XAH0GCbF8nI/VlmUB5q1AxSo6HC', 'inactive@stagger.cz', '123456789', 0, '2023-04-16 00:13:47', 'inactive', NULL, NULL);

/* ROLE TABLE */
INSERT INTO "role" ("id", "name")
VALUES (1, 'ADMIN');
INSERT INTO "role" ("id", "name")
VALUES (2, 'MODERATOR');
INSERT INTO "role" ("id", "name")
VALUES (3, 'USER');

/* USER x ROLE mapping */
INSERT INTO "roles_users_map" ("id", "user_id", "role_id")
VALUES (1, 1, 1);
INSERT INTO "roles_users_map" ("id", "user_id", "role_id")
VALUES (2, 1, 2);
INSERT INTO "roles_users_map" ("id", "user_id", "role_id")
VALUES (3, 2, 3);
INSERT INTO "roles_users_map" ("id", "user_id", "role_id")
VALUES (4, 2, 3);

/* UNIVERSITY TABLE */
INSERT INTO "university" ("id", "name", "created_at", "created_by", "updated_at", "updated_by")
VALUES (1, 'Západočeská univerzita', '2023-04-16 00:13:47', 'system', NULL, NULL);

/* SUBJECT TABLE */
INSERT INTO "subject" ("id", "university_id", "department", "name")
VALUES (1, 1, 'KEM', 'EK1');
INSERT INTO "subject" ("id", "university_id", "department", "name")
VALUES (2, 1, 'KIV', 'PPA2');

/* USER x UNIVERSITY mapping */
INSERT INTO "universities_users_map" ("id", "user_id", "university_id")
VALUES (1, 2, 1);

INSERT INTO "notification" ("id", "user_id", "type", "icon", "message", "is_read", "created_at", "created_by", "updated_at", "updated_by")
VALUES (1, 2, 'PRIMARY', 'ACTIVITY', 'SUCCESS_ENROLLMENT', 0, '2023-05-16 10:13:47', 'system', NULL, NULL);
INSERT INTO "notification" ("id", "user_id", "type", "icon", "message", "is_read", "created_at", "created_by", "updated_at", "updated_by")
VALUES (2, 2, 'DANGER', 'BELL', 'DANGER_MESSAGE', 0, '2023-05-18 10:13:47', 'system', NULL, NULL);
INSERT INTO "notification" ("id", "user_id", "type", "icon", "message", "is_read", "created_at", "created_by", "updated_at", "updated_by")
VALUES (3, 1, 'PRIMARY', 'ACTIVITY', 'SUCCESS_MESSAGE', 0, '2023-05-19 10:13:47', 'system', NULL, NULL);
INSERT INTO "notification" ("id", "user_id", "type", "icon", "message", "is_read", "created_at", "created_by", "updated_at", "updated_by")
VALUES (4, 2, 'PRIMARY', 'ACTIVITY', 'SUCCESS_MESSAGE', 0, '2023-05-22 15:10:17', 'system', NULL, NULL);