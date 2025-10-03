# Аттестация Программная инженерия. Разработка ПО (Java для начинающих специалистов)

## Описание
Приложение для учёта заказов с PostgreSQL, JDBC, Maven и Flyway.

## Запуск
1. PostgreSQL (v12+), создать БД `postgres` с user/password из src/main/resources/application.properties
- Открыть psql и выполнить команду `psql -h localhost -U MK27w5FZcEqh -d postgres`, ввести пароль `HmfeLwodG981` 
- Установить кодировку `SET client_encoding TO 'UTF8';`
- Инициализировать создание БД `\i sql/schema.sql`
- Если что-то пошло не так с созданием БД:
`DROP TABLE IF EXISTS orders CASCADE;`
`DROP TABLE IF EXISTS order CASCADE;`
`DROP TABLE IF EXISTS "order" CASCADE;`
`DROP TABLE IF EXISTS order_status CASCADE;`
`DROP TABLE IF EXISTS customer CASCADE;`
`DROP TABLE IF EXISTS product CASCADE;`
`DROP TABLE IF EXISTS flyway_schema_history;`
- Если таблицы были удалены Flyway может ругаться, для этого необходимо в коде к Flyway добавить `.baselineOnMigrate(true)`
2. IDEA терминал/Windows Shell `mvn clean package`
3. Windows Shell `java -jar target/attestation-order-list-001.jar`

## Файлы
- cc9fde864f4b4b05bc31596eb3915bad.pdf: Аттестационное задание, требования к оформлению

- pom.xml: Maven config
`Настройка Java 17`
`Зависимости: PostgreSQL JDBC Driver, Flyway для миграций`
`Плагины для компиляции и создания исполняемого JAR`
`Настройки кодировки и манифеста`

- sql/schema.sql: Схема БД и данные
`Создание таблиц: product, customer, orders, order_status`
`Первичные и внешние ключи, ограничения CHECK`
`Индексы для оптимизации запросов`
`Комментарии к таблицам и полям`
`10+ тестовых записей в каждой таблице`

- sql/test-queries.sql: SQL-запросы
`6 запросов на чтение (SELECT с JOIN, агрегатами, сортировкой)`
`3 запроса на изменение (UPDATE)`
`2 запроса на удаление (DELETE)`
`Примеры: топ-3 товара, заказы за 7 дней, обновление цен`

- src/main/java/App.java: Основной класс
`Подключение к PostgreSQL через JDBC`
`Автоматический запуск миграций Flyway`
`Демонстрация CRUD операций (вставка, чтение, обновление, удаление)`
`Работа в транзакциях с commit/rollback`
`Форматированный вывод результатов в консоль`

- src/main/resources/application.properties: Настройки БД
`URL базы данных PostgreSQL`
`Имя пользователя и пароль`
`Настройки для JDBC соединения`

- src/main/resources/db/migration/V001__migra.sql: Миграция Flyway
`DDL скрипты создания всех таблиц`
`Ограничения целостности данных`
`Индексы для улучшения производительности`
`Наполнение тестовыми данными`
`SQL скрипт выполняется автоматически при запуске приложения`

- Статусы:
1. Создан
2. Оплачен
3. Доставлен
4. Отменен
5. В обработке
6. Готов к выдаче
7. В пути
8. Получен
9. Возвращен
10. Архив

## Скриншоты
1. Все заказы за последние 7 дней - testsql/1Days.png / [смотреть](https://github.com/chaurd9/educationjava/blob/attestation/attestation-order-list/screenshots/testsql/1Days.png)
2. Топ-3 самых дорогих товара - testsql/2Top.png / [смотреть](https://github.com/chaurd9/educationjava/blob/attestation/attestation-order-list/screenshots/testsql/2Top.png)
3. Список клиентов и количество заказов - testsql/3ListClients.png / [смотреть](https://github.com/chaurd9/educationjava/blob/attestation/attestation-order-list/screenshots/testsql/3ListClients.png)
4. Изменить цену молока - testsql/4UpdatePriceMilkDO.png и 4UpdatePriceMilkPOSLE.png / [смотреть до](https://github.com/chaurd9/educationjava/blob/attestation/attestation-order-list/screenshots/testsql/4UpdatePriceMilkDO.png) / [после](https://github.com/chaurd9/educationjava/blob/attestation/attestation-order-list/screenshots/testsql/4UpdatePriceMilkPOSLE.png)
5. Увеличить количество хлеба - testsql/5UpdateAmountBreadDO.png и 5UpdateAmountBreadPOSLE.png / [смотреть до](https://github.com/chaurd9/educationjava/blob/attestation/attestation-order-list/screenshots/testsql/5UpdateAmountBreadDO.png) / [после](https://github.com/chaurd9/educationjava/blob/attestation/attestation-order-list/screenshots/testsql/5UpdateAmountBreadPOSLE.png)
6. Удалить клиента без заказов - testsql/6DeleteEmptyDO.png и 6DeleteEmptyPOSLE.png / [смотреть до](https://github.com/chaurd9/educationjava/blob/attestation/attestation-order-list/screenshots/testsql/6DeleteEmptyDO.png) / [после](https://github.com/chaurd9/educationjava/blob/attestation/attestation-order-list/screenshots/testsql/6DeleteEmptyPOSLE.png)
7. Список всех товаров по категориям - testsql/7ListCateg.png / [смотреть](https://github.com/chaurd9/educationjava/blob/attestation/attestation-order-list/screenshots/testsql/7ListCateg.png)
8. Удаление всех заказов старше года - testsql/8DeleteOldestDO.png и 8DeleteOldestPOSLE.png / [смотреть до](https://github.com/chaurd9/educationjava/blob/attestation/attestation-order-list/screenshots/testsql/8DeleteOldestDO.png) / [после](https://github.com/chaurd9/educationjava/blob/attestation/attestation-order-list/screenshots/testsql/8DeleteOldestPOSLE.png)
9. Изменение статуса заказа - testsql/9ChangeStatusDO.png и 9ChangeStatusPOSLE.png / [смотреть до](https://github.com/chaurd9/educationjava/blob/attestation/attestation-order-list/screenshots/testsql/9ChangeStatusDO.png) / [после](https://github.com/chaurd9/educationjava/blob/attestation/attestation-order-list/screenshots/testsql/9ChangeStatusPOSLE.png)
10. Количество товаров на складе - testsql/10Quantity.png / [смотреть](https://github.com/chaurd9/educationjava/blob/attestation/attestation-order-list/screenshots/testsql/10Quantity.png)
11. Подключение к БД PSQL IDEA - condbidea.png / [смотреть](https://github.com/chaurd9/educationjava/blob/attestation/attestation-order-list/screenshots/condbidea.png)
12. Диаграмма - DBeaverDiag.png / [смотреть](https://github.com/chaurd9/educationjava/blob/attestation/attestation-order-list/screenshots/DBeaverDiag.png)
13. Установленный Docker Desktop - docker-install.png / [смотреть](https://github.com/chaurd9/educationjava/blob/attestation/attestation-order-list/screenshots/docker-install.png)
14. PSQL 12 - psql-install.png / [смотреть](https://github.com/chaurd9/educationjava/blob/attestation/attestation-order-list/screenshots/psql-install.png)
15. Скомпилированный .jar с помощью Maven, примеры с CRUD-операциями, и миграции Flyway - MavenJarMigrationCRUD.png / [смотреть](https://github.com/chaurd9/educationjava/blob/attestation/attestation-order-list/screenshots/MavenJarMigrationCRUD.png)

## Обновления
03.10.2025
- Добавлен столбец с состоянием заказа
- Добавлено описание для CRUD-операций из файла App.java
- Добавлены новые клиенты и заказы
- Исправлены фамилии клиентов
- Добавлены дополнительные запросы в SQL для файла test-queries.sql, для отслеживания ДО и ПОСЛЕ
- Обновлены скриншоты для тестовых запросов SQL и .jar-приложения