/* USER TABLE (password = username) */
INSERT INTO "user" ("id", "username", "password", "email_address", "phone_number", "language", "is_active", "created_at", "created_by", "updated_at", "updated_by")
VALUES (1, 'admin', '$2a$12$V/t1fRb1qvpOxGBoEHhDMOCgEpK7FBtLl0q9omX2iMbLjpfxI0c0W', 'admin@stagger.cz', '123456789', 'CS', 1, '2023-04-16 00:13:47', 'admin', NULL, NULL);
INSERT INTO "user" ("id", "username", "password", "email_address", "phone_number", "language", "is_active", "created_at", "created_by", "updated_at", "updated_by")
VALUES (2, 'user', '$2a$12$kOXYV3zxUWFeJHXueBvXremslfkq.e9Zqfr5Jdm4vD12SfZ9nZDwS', 'user@stagger.cz', '123456789', 'CS', 1, '2023-04-16 00:13:47', 'user', NULL, NULL);
INSERT INTO "user" ("id", "username", "password", "email_address", "phone_number", "language", "is_active", "created_at", "created_by", "updated_at", "updated_by")
VALUES (3, 'inactive', '$2a$12$j4Zm41Uy9VYNXisppiCWSOTGS9XAH0GCbF8nI/VlmUB5q1AxSo6HC', 'inactive@stagger.cz', '123456789', 'CS', 0, '2023-04-16 00:13:47', 'inactive', NULL, NULL);
