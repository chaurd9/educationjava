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
SELECT description, price FROM product WHERE description = 'Молоко'; -- Перед обновлением: показать текущую цену
UPDATE product SET price = 85.99 WHERE description = 'Молоко';
SELECT description, price FROM product WHERE description = 'Молоко'; -- После обновления: показать новую цену

-- 5. Увеличить количество хлеба на складе -- Индексы необязательны, так как это UPDATE по значению description
SELECT description, quantity FROM product WHERE description = 'Хлеб'; -- Перед обновлением: показать текущее количество
UPDATE product SET quantity = quantity + 10 WHERE description = 'Хлеб';
SELECT description, quantity FROM product WHERE description = 'Хлеб'; -- После обновления: показать новое количество

-- 6. Удалить клиентов без заказов -- Использует индекс idx_orders_customer_id для субзапроса
SELECT c.id, c.first_name, c.last_name -- Перед удалением: показать клиентов без заказов
FROM customer c
WHERE c.id NOT IN (SELECT DISTINCT customer_id FROM orders);
DELETE FROM customer WHERE id NOT IN (SELECT DISTINCT customer_id FROM orders); -- Удаление клиента без заказа (Игорь)
SELECT c.id, c.first_name, c.last_name -- После удаления: проверить, что таких клиентов больше нет (должен вернуть 0 строк)
FROM customer c
WHERE c.id NOT IN (SELECT DISTINCT customer_id FROM orders);

-- 7. Список всех товаров по категории -- Индексы необязательны, так как это простой SELECT
SELECT category, description, price FROM product ORDER BY category;

-- 8. Удаление всех заказов старше месяца -- Использует индекс idx_orders_order_date для фильтрации по дате
SELECT id, order_date FROM orders WHERE order_date < NOW() - INTERVAL '30 days'; -- Перед удалением: показать заказы старше 30 дней
DELETE FROM orders WHERE order_date < NOW() - INTERVAL '30 days';
SELECT id, order_date FROM orders WHERE order_date < NOW() - INTERVAL '30 days'; -- После удаления: проверить, что таких заказов больше нет (должен вернуть 0 строк)

-- 9. Изменение статуса заказа -- Использует индекс idx_orders_status для обновления статуса
SELECT o.id, os.status_name FROM orders o JOIN order_status os ON o.status = os.id WHERE o.id = 1; -- Перед обновлением: показать текущий статус заказа с ID=1
UPDATE orders SET status = 3 WHERE id = 1;
SELECT o.id, os.status_name FROM orders o JOIN order_status os ON o.status = os.id WHERE o.id = 1; -- После обновления: показать новый статус

-- 10. Проверка остатков на складе -- Индексы необязательны, так как это простой SELECT
SELECT description, quantity FROM product WHERE quantity < 50;