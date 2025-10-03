import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.io.InputStream;
import java.time.format.DateTimeFormatter;
import org.flywaydb.core.Flyway;

public class App {
    public static void main(String[] args) {
        Connection conn = null;
        String newProductDescription = null;
        String newCustomerName = null;

        try (InputStream input = App.class.getClassLoader().getResourceAsStream("application.properties")) {
            Properties props = new Properties();
            props.load(input);

            String url = props.getProperty("db.url");
            String user = props.getProperty("db.user");
            String password = props.getProperty("db.password");

            Flyway flyway = Flyway.configure()
                    .dataSource(url, user, password)
                    .load();
            flyway.migrate();
            System.out.println("====================================");
            System.out.println("Миграции БД Flywaydb выполнены успешно.");
            System.out.println("====================================");

            conn = DriverManager.getConnection(url, user, password);
            conn.setAutoCommit(false);

            int newProductId;
            try (PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO product(description, price, quantity, category) VALUES (?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, "Сметана");
                ps.setBigDecimal(2, new BigDecimal("89.99"));
                ps.setInt(3, 25);
                ps.setString(4, "Молочные продукты");
                ps.executeUpdate();
                ResultSet rs = ps.getGeneratedKeys();
                newProductId = rs.next() ? rs.getInt(1) : -1;
                newProductDescription = "Сметана";
                System.out.println("Вставлен новый товар с ID: " + newProductId + " (" + newProductDescription + ")");
            }

            int newCustomerId;
            try (PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO customer(first_name, last_name, phone, email) VALUES (?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, "Тестовый");
                ps.setString(2, "Клиент");
                ps.setString(3, "123456");
                ps.setString(4, "new@example.com");
                ps.executeUpdate();
                ResultSet rs = ps.getGeneratedKeys();
                newCustomerId = rs.next() ? rs.getInt(1) : -1;
                newCustomerName = "Тестовый";
                System.out.println("Вставлен новый покупатель с ID: " + newCustomerId + " (" + newCustomerName + ")");
            } catch (SQLException e) {
                if (e.getSQLState().equals("23505")) {
                    System.out.println("Ошибка: Email 'new@example.com' уже существует. Используйте другой email.");
                    newCustomerId = -1;
                } else {
                    throw e;
                }
            }

            int newOrderId;
            try (PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO orders(product_id, customer_id, quantity, status) VALUES (?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, newProductId);
                ps.setInt(2, newCustomerId);
                ps.setInt(3, 1);
                ps.setInt(4, 1);
                ps.executeUpdate();
                ResultSet rs = ps.getGeneratedKeys();
                newOrderId = rs.next() ? rs.getInt(1) : -1;
                System.out.println("Создан новый заказ с ID: " + newOrderId);
            }
            System.out.println("====================================");
            try (Statement st = conn.createStatement()) {
                ResultSet rs = st.executeQuery(
                        "SELECT o.id, c.first_name, p.description, o.quantity, os.status_name, o.order_date " +
                                "FROM orders o " +
                                "JOIN customer c ON o.customer_id = c.id " +
                                "JOIN product p ON o.product_id = p.id " +
                                "JOIN order_status os ON o.status = os.id " +
                                "ORDER BY o.order_date DESC LIMIT 5");
                System.out.println("Последние 5 заказов:");
                List<String> rows = new ArrayList<>();
                int maxLength = 0;
                while (rs.next()) {
                    String line = String.format("%-3d | %-10s | %-15s | %-6s | %-15s | %s",
                            rs.getInt("id"),
                            rs.getString("first_name"),
                            rs.getString("description"),
                            rs.getInt("quantity") + " шт.",
                            rs.getString("status_name"),
                            rs.getTimestamp("order_date").toLocalDateTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")));
                    rows.add(line);
                    maxLength = Math.max(maxLength, line.length());
                }
                String separator = "-".repeat(maxLength);
                System.out.println(separator);
                System.out.printf("%-3s | %-10s | %-15s | %-6s | %-15s | %s%n", "№", "Имя", "Продукт", "Кол", "Статус", "Дата");
                System.out.println(separator);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
                for (String row : rows) {
                    System.out.println(row);
                }
                System.out.println(separator);
            }
            try (PreparedStatement ps = conn.prepareStatement(
                    "UPDATE product SET price = ?, quantity = quantity - ? WHERE id = ?")) {
                ps.setBigDecimal(1, new BigDecimal("99.99"));
                ps.setInt(2, 1);
                ps.setInt(3, newProductId);
                ps.executeUpdate();
                System.out.println("Обновлена цена и количество для товара ID: " + newProductId + " (" + newProductDescription + ")");
            }
            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM orders WHERE id = ?")) {
                ps.setInt(1, newOrderId);
                ps.executeUpdate();
                System.out.println("Удалён заказ ID: " + newOrderId);
            }
            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM customer WHERE id = ?")) {
                ps.setInt(1, newCustomerId);
                ps.executeUpdate();
                System.out.println("Удалён покупатель ID: " + newCustomerId + " (" + newCustomerName + ")");
            }
            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM product WHERE id = ?")) {
                ps.setInt(1, newProductId);
                ps.executeUpdate();
                System.out.println("Удалён товар ID: " + newProductId  + " (" + newProductDescription + ")");
            }
            try (Statement st = conn.createStatement()) {
                ResultSet rs = st.executeQuery(
                        "SELECT o.id, c.first_name, p.description, o.quantity, os.status_name, o.order_date " +
                                "FROM orders o " +
                                "JOIN customer c ON o.customer_id = c.id " +
                                "JOIN product p ON o.product_id = p.id " +
                                "JOIN order_status os ON o.status = os.id " +
                                "ORDER BY o.order_date DESC LIMIT 5");
                List<String> rows = new ArrayList<>();
                int maxLength = 0;
                while (rs.next()) {
                    String line = String.format("%-3d | %-10s | %-15s | %-6s | %-15s | %s",
                            rs.getInt("id"),
                            rs.getString("first_name"),
                            rs.getString("description"),
                            rs.getInt("quantity") + " шт.",
                            rs.getString("status_name"),
                            rs.getTimestamp("order_date").toLocalDateTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")));
                    rows.add(line);
                    maxLength = Math.max(maxLength, line.length());
                }
                String separator = "-".repeat(maxLength);
                System.out.println(separator);
                System.out.printf("%-3s | %-10s | %-15s | %-6s | %-15s | %s%n", "№", "Имя", "Продукт", "Кол", "Статус", "Дата");
                System.out.println(separator);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
                for (String row : rows) {
                    System.out.println(row);
                }
                System.out.println(separator);
            }

            conn.commit();
            System.out.println("Все операции выполнены успешно!");

        } catch (Exception ex) {
            if (conn != null) {
                try {
                    conn.rollback();
                    System.out.println("Транзакция откачена из-за ошибки");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            ex.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}