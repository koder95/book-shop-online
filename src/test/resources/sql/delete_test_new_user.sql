DELETE FROM cart_items WHERE shopping_cart_id = (
    SELECT user_id FROM shopping_carts WHERE user_id = (
        SELECT id FROM users WHERE email = 'testuser@example.com'
    )
);
DELETE FROM shopping_carts WHERE user_id = (SELECT id FROM users WHERE email = 'testuser@example.com');
DELETE FROM users_roles WHERE user_id = (SELECT id FROM users WHERE email = 'testuser@example.com');
DELETE FROM users WHERE email = 'testuser@example.com';
