INSERT INTO users (email, first_name, last_name, password)
VALUES ('testuser@example.com', 'Test', 'User', 'password');
INSERT INTO users_roles (user_id, role_id)
VALUES ((SELECT id FROM users WHERE email = 'testuser@example.com'), 2);
