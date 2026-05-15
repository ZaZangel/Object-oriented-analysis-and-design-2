import java.sql.*;       // Импорт классов для работы с JDBC
import java.util.ArrayList; // Импорт реализации списка
import java.util.List;   // Импорт интерфейса списка

public class CustomerRepository { // Класс доступа к данным (Data Access Layer)
    // Строка подключения к SQLite-файлу в текущей директории
    private static final String URL = "jdbc:sqlite:customers.db";

    // Конструктор автоматически инициализирует БД и создает таблицы при первом запуске
    public CustomerRepository() {
        // try-with-resources гарантирует закрытие Connection и Statement
        try (Connection conn = DriverManager.getConnection(URL); 
             Statement stmt = conn.createStatement()) {
            
            // SQL-запрос создания таблицы клиентов, если её нет
            String createCustomersTable = 
                "CREATE TABLE IF NOT EXISTS customers (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, " + "email TEXT, " + "phone TEXT)";
            
            // SQL-запрос создания таблицы заказов с внешним ключом
            String createOrdersTable = 
                "CREATE TABLE IF NOT EXISTS orders (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "customer_id INTEGER, " + "amount REAL, " + 
                "date TEXT, " + "category TEXT)";

            stmt.execute(createCustomersTable); // Выполнение DDL-запроса
            stmt.execute(createOrdersTable);    // Выполнение DDL-запроса
            
        } catch (SQLException e) { 
            e.printStackTrace(); // Вывод трассировки при ошибке подключения
        }
    }

    // Получение всех клиентов (обертка над поиском)
    public List<CustomerEntity> getAllCustomers() {
        return searchCustomers(""); // Пустая строка вернет все записи
    }

    // Поиск клиентов по подстроке имени с использованием PreparedStatement (защита от SQL-инъекций)
    public List<CustomerEntity> searchCustomers(String name) {
        List<CustomerEntity> list = new ArrayList<>(); // Список для результата
        String sql = "SELECT * FROM customers WHERE name LIKE ?"; // Параметризированный запрос
        try (Connection conn = DriverManager.getConnection(URL); 
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + name + "%"); // Подстановка wildcard-символов
            ResultSet rs = pstmt.executeQuery();  // Выполнение SELECT
            while (rs.next()) { // Итерация по результатам выборки
                // Маппинг строки ResultSet в объект Entity
                list.add(new CustomerEntity(
                    rs.getLong("id"), 
                    rs.getString("name"), 
                    rs.getString("email"),
                    rs.getString("phone")
                ));
            }
        } catch (SQLException e) { 
            e.printStackTrace(); 
        }
        return list; // Возврат списка объектов
    }

    // Вставка нового клиента
    public void saveCustomer(String name, String email, String phone) {
        String sql = "INSERT INTO customers(name, email, phone) VALUES(?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL); 
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, email);
            pstmt.setString(3, phone);
            pstmt.executeUpdate(); // Выполнение INSERT
        } catch (SQLException e) { e.printStackTrace(); }
    }

    // Обновление данных существующего клиента
    public void updateCustomer(long id, String name, String email, String phone) {
        String sql = "UPDATE customers SET name = ?, email = ?, phone = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL); 
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, email);
            pstmt.setString(3, phone);
            pstmt.setLong(4, id); // Привязка условия WHERE
            pstmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    // Удаление клиента и каскадное удаление его заказов
    public void deleteCustomer(long id) {
        // Сначала чистим связанные записи в orders
        try (Connection conn = DriverManager.getConnection(URL); 
             PreparedStatement pstmt = conn.prepareStatement("DELETE FROM orders WHERE customer_id = ?")) {
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
        // Затем удаляем саму запись клиента
        try (Connection conn = DriverManager.getConnection(URL); 
             PreparedStatement pstmt = conn.prepareStatement("DELETE FROM customers WHERE id = ?")) {
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    // Получение списка заказов конкретного клиента
    public List<OrderEntity> getOrdersByCustomer(long customerId) {
        List<OrderEntity> list = new ArrayList<>();
        String sql = "SELECT * FROM orders WHERE customer_id = ? ORDER BY date DESC"; // Сортировка по дате
        try (Connection conn = DriverManager.getConnection(URL); 
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, customerId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(new OrderEntity(
                    rs.getLong("id"), 
                    rs.getLong("customer_id"), 
                    rs.getDouble("amount"), 
                    rs.getString("date"),
                    rs.getString("category")
                ));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    // Добавление нового заказа
    public void saveOrder(long customerId, double amount, String date, String category) {
        String sql = "INSERT INTO orders(customer_id, amount, date, category) VALUES(?,?,?,?)";
        try (Connection conn = DriverManager.getConnection(URL); 
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, customerId);
            pstmt.setDouble(2, amount);
            pstmt.setString(3, date);
            pstmt.setString(4, category);
            pstmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    // Удаление конкретного заказа
    public void deleteOrder(long orderId) {
        try (Connection conn = DriverManager.getConnection(URL); 
             PreparedStatement pstmt = conn.prepareStatement("DELETE FROM orders WHERE id = ?")) {
            pstmt.setLong(1, orderId);
            pstmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    // Полная очистка базы (сброс автоинкремента)
    public void deleteAll() {
        try (Connection conn = DriverManager.getConnection(URL); 
             Statement stmt = conn.createStatement()) {
            stmt.execute("DELETE FROM customers");
            stmt.execute("DELETE FROM orders");
            stmt.execute("DELETE FROM sqlite_sequence WHERE name='customers' OR name='orders'");
        } catch (SQLException e) { e.printStackTrace(); }
    }
}