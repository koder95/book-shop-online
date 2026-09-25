INSERT INTO shopping_carts (user_id)
VALUES (SELECT id FROM users WHERE email = 'testuser@example.com');
INSERT INTO cart_items (id, book_id, shopping_cart_id, quantity)
VALUES (1, (SELECT id FROM books WHERE title = 'Test Book'), (SELECT id FROM users WHERE email = 'testuser@example.com'), 1);