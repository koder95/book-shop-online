INSERT INTO users (id, email, first_name, last_name, password, is_deleted)
VALUES (2, 'testuser@example.com', 'Test', 'User', 'password', false);
INSERT INTO users_roles (user_id, role_id)
VALUES (2, 2);
