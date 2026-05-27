DELETE FROM books_categories WHERE book_id = 1;
DELETE FROM books WHERE id = 1;
ALTER TABLE books ALTER COLUMN id RESTART WITH 1;
