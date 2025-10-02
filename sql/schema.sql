CREATE TABLE IF NOT EXISTS product (
    id SERIAL PRIMARY KEY,
    description VARCHAR(255) NOT NULL,
    price NUMERIC(10,2) CHECK (price >= 0),
    quantity INT CHECK (quantity >= 0),
    category VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS customer (
    id SERIAL PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    phone VARCHAR(20),
    email VARCHAR(100) UNIQUE
);

CREATE TABLE IF NOT EXISTS order_status (
    id SERIAL PRIMARY KEY,
    status_name VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS orders (
    id SERIAL PRIMARY KEY,
    product_id INT REFERENCES product(id),
    customer_id INT REFERENCES customer(id),
    order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    quantity INT CHECK (quantity > 0),
    status INT REFERENCES order_status(id)
);

CREATE INDEX idx_orders_product_id ON orders(product_id);
CREATE INDEX idx_orders_customer_id ON orders(customer_id);
CREATE INDEX idx_orders_status ON orders(status);
CREATE INDEX idx_orders_order_date ON orders(order_date);

COMMENT ON TABLE product IS 'Таблица для хранения товаров продуктового магазина';
COMMENT ON COLUMN product.id IS 'Первичный ключ';
COMMENT ON COLUMN product.description IS 'Описание товара';
COMMENT ON COLUMN product.price IS 'Цена товара';
COMMENT ON COLUMN product.quantity IS 'Количество на складе';
COMMENT ON COLUMN product.category IS 'Категория товара';

COMMENT ON TABLE customer IS 'Таблица для хранения клиентов';
COMMENT ON COLUMN customer.id IS 'Первичный ключ';
COMMENT ON COLUMN customer.first_name IS 'Имя клиента';
COMMENT ON COLUMN customer.last_name IS 'Фамилия клиента';
COMMENT ON COLUMN customer.phone IS 'Телефон клиента';
COMMENT ON COLUMN customer.email IS 'Email клиента';

COMMENT ON TABLE order_status IS 'Справочник статусов заказов';
COMMENT ON COLUMN order_status.id IS 'Первичный ключ';
COMMENT ON COLUMN order_status.status_name IS 'Имя статуса';

COMMENT ON TABLE orders IS 'Таблица для хранения заказов';
COMMENT ON COLUMN orders.id IS 'Первичный ключ';
COMMENT ON COLUMN orders.product_id IS 'Внешний ключ на товар';
COMMENT ON COLUMN orders.customer_id IS 'Внешний ключ на клиента';
COMMENT ON COLUMN orders.order_date IS 'Дата заказа';
COMMENT ON COLUMN orders.quantity IS 'Количество';
COMMENT ON COLUMN orders.status IS 'Внешний ключ на статус заказа';

INSERT INTO product (description, price, quantity, category) VALUES
('Хлеб', 49.99, 100, 'Хлебобулочные'),
('Молоко', 89.99, 50, 'Молочные продукты'),
('Яйца', 129.99, 200, 'Яйца'),
('Сыр', 299.99, 30, 'Молочные продукты'),
('Курица', 199.99, 40, 'Мясные продукты'),
('Колбаса', 399.99, 20, 'Мясные продукты'),
('Картофель', 39.99, 150, 'Овощи'),
('Морковь', 29.99, 100, 'Овощи'),
('Яблоки', 69.99, 80, 'Фрукты'),
('Бананы', 89.99, 60, 'Фрукты');

INSERT INTO customer (first_name, last_name, phone, email) VALUES
('Александр', 'Хлеб', '123456789', 'alex.hleb@example.com'),
('Андрей', 'Молоко', '987654321', 'andrey.moloko@example.com'),
('Анна', 'Яйца', '111222333', 'anna.yaytsa@example.com'),
('Алиса', 'Сыр', '444555666', 'alisa.syr@example.com'),
('Артем', 'Яблоки', '777888999', 'artem.yabloki@example.com'),
('Арина', 'Бананы', '000111222', 'arina.banany@example.com'),
('Анатолий', 'Картофель', '333444555', 'anatoliy.kartofel@example.com'),
('Алексей', 'Морковь', '666777888', 'alexey.morkov@example.com'),
('Антон', 'Колбаса', '999000111', 'anton.kolbasa@example.com'),
('Алена', 'Бойко', '222333444', 'alena.boyko@example.com');

INSERT INTO order_status (status_name) VALUES
('Создан'), ('Оплачен'), ('Доставлен'), ('Отменен'), ('В обработке'),
('Готов к выдаче'), ('В пути'), ('Получен'), ('Возвращен'), ('Архив');

INSERT INTO orders (product_id, customer_id, quantity, status, order_date) VALUES
((SELECT id FROM product WHERE description = 'Хлеб'), (SELECT id FROM customer WHERE first_name = 'Александр'), 2, 1, '2025-10-01 10:00:00'),
((SELECT id FROM product WHERE description = 'Молоко'), (SELECT id FROM customer WHERE first_name = 'Андрей'), 1, 2, '2025-09-28 14:30:00'),
((SELECT id FROM product WHERE description = 'Яйца'), (SELECT id FROM customer WHERE first_name = 'Анна'), 3, 3, '2025-09-25 09:15:00'),
((SELECT id FROM product WHERE description = 'Сыр'), (SELECT id FROM customer WHERE first_name = 'Алиса'), 1, 4, '2025-09-20 16:45:00'),
((SELECT id FROM product WHERE description = 'Яблоки'), (SELECT id FROM customer WHERE first_name = 'Артем'), 5, 5, '2025-09-15 11:20:00'),
((SELECT id FROM product WHERE description = 'Бананы'), (SELECT id FROM customer WHERE first_name = 'Арина'), 4, 6, '2025-09-10 13:00:00'),
((SELECT id FROM product WHERE description = 'Картофель'), (SELECT id FROM customer WHERE first_name = 'Анатолий'), 10, 7, '2025-09-05 08:30:00'),
((SELECT id FROM product WHERE description = 'Морковь'), (SELECT id FROM customer WHERE first_name = 'Алексей'), 6, 8, '2025-08-25 17:00:00'),
((SELECT id FROM product WHERE description = 'Колбаса'), (SELECT id FROM customer WHERE first_name = 'Антон'), 2, 9, '2025-08-15 12:10:00'),
((SELECT id FROM product WHERE description = 'Курица'), (SELECT id FROM customer WHERE first_name = 'Алена'), 1, 10, '2025-08-01 15:40:00');