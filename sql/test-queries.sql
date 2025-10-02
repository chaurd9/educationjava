-- 1. Все заказы за последние 7 дней -- Использует индекс idx_orders_order_date для фильтрации по дате
SELECT o.id, c.first_name, p.description, o.quantity, o.order_date
FROM orders o
JOIN customer c ON o.customer_id = c.id
JOIN product p ON o.product_id = p.id
WHERE o.order_date > NOW() - INTERVAL '7 days';

-- 2. Топ-3 популярных товара -- Использует индекс idx_orders_product_id для JOIN с product
SELECT p.description, SUM(o.quantity) as total
FROM orders o
JOIN product p ON o.product_id = p.id
GROUP BY p.description
ORDER BY total DESC
LIMIT 3;

-- 3. Список клиентов и количество их заказов -- Использует индекс idx_orders_customer_id для JOIN с customer
SELECT c.first_name, c.last_name, COUNT(o.id) as order_count
FROM customer c
LEFT JOIN orders o ON c.id = o.customer_id
GROUP BY c.first_name, c.last_name
ORDER BY c.first_name;

-- 4. Обновить цену у молока -- Индексы необязательны, так как это UPDATE по значению description
UPDATE product SET price = 85.99 WHERE description = 'Молоко';

-- 5. Увеличить количество хлеба на складе -- Индексы необязательны, так как это UPDATE по значению description
UPDATE product SET quantity = quantity + 10 WHERE description = 'Хлеб';

-- 6. Удалить клиентов без заказов -- Использует индекс idx_orders_customer_id для субзапроса
DELETE FROM customer WHERE id NOT IN (SELECT DISTINCT customer_id FROM orders);

-- 7. Список всех товаров по категории -- Индексы необязательны, так как это простой SELECT
SELECT category, description, price FROM product ORDER BY category;

-- 8. Удаление всех заказов старше месяца -- Использует индекс idx_orders_order_date для фильтрации по дате
DELETE FROM orders WHERE order_date < NOW() - INTERVAL '30 days';

-- 9. Изменение статуса заказа -- Использует индекс idx_orders_status для обновления статуса
UPDATE orders SET status = 3 WHERE id = 1;

-- 10. Проверка остатков на складе -- Индексы необязательны, так как это простой SELECT
SELECT description, quantity FROM product WHERE quantity < 50;