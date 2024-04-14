-- Создание таблицы authors
CREATE TABLE authors (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

-- Создание таблицы publishers
CREATE TABLE publishers (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    address VARCHAR(255)
);

-- Создание таблицы books
CREATE TABLE books (
    id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL UNIQUE,
    publisher_id INTEGER,
    price numeric,
    FOREIGN KEY (publisher_id) REFERENCES publishers(id)
);

-- Создание таблицы book_authors для связи многие ко многим между книгами и авторами
CREATE TABLE book_authors (
    book_id INTEGER,
    author_id INTEGER,
    PRIMARY KEY (book_id, author_id),
    FOREIGN KEY (book_id) REFERENCES books(id),
    FOREIGN KEY (author_id) REFERENCES authors(id)
);

-- Вставка начальных данных
INSERT INTO authors (name) VALUES ('Author 1'), ('Author 2'), ('Author 3');
INSERT INTO publishers (name, address) VALUES ('Publisher 1', 'Address 1'), ('Publisher 2', 'Address 2');
INSERT INTO books (title, publisher_id, price) VALUES ('Book 1', 1, 10.0), ('Book 2', 2, 15.0);
INSERT INTO book_authors (book_id, author_id) VALUES (1, 1), (1, 2), (2, 3);