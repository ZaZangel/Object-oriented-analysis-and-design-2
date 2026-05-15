import java.util.List; // Импорт для работы со списками
//Converter (Преобразователь) — это структурный паттерн, который выделяет логику
//преобразования данных в отдельный класс, изолируя модель хранения (Entity) от модели представления (DTO).

public class Main { // Точка входа в приложение
    public static void main(String[] args) {
        // Инициализация слоя доступа к данным
        CustomerRepository repository = new CustomerRepository();
        // Инициализация слоя преобразования данных
        CustomerConverter converter = new CustomerConverter();

        // Проверка наличия данных в БД
        List<CustomerEntity> existingCustomers = repository.getAllCustomers();
        
        // Если база пуста, генерируем демо-данные для наглядности
        if (existingCustomers.isEmpty()) {
            System.out.println("Main: База пуста. Генерируем тестовые данные...");
            
            // Сохранение 8 тестовых клиентов
            repository.saveCustomer("Иванов Петр Сергеевич", "ivanov@email.ru", "+7-900-123-45-67");
            repository.saveCustomer("Смирнова Анна Владимировна", "smirnova@email.ru", "+7-900-765-43-21");
            repository.saveCustomer("Козлов Дмитрий Александрович", "kozlov@email.ru", "+7-900-111-22-33");
            repository.saveCustomer("Новикова Елена Игоревна", "novikova@email.ru", "+7-900-444-55-66");
            repository.saveCustomer("Волков Максим Олегович", "volkov@email.ru", "+7-900-777-88-99");
            repository.saveCustomer("Лебедева Ольга Павловна", "lebedeva@email.ru", "+7-900-222-33-44");
            repository.saveCustomer("Соколов Артём Дмитриевич", "sokolov@email.ru", "+7-900-555-66-77");
            repository.saveCustomer("Кузнецова Марина Сергеевна", "kuznetsova@email.ru", "+7-900-888-99-00");

            // Получаем список для привязки заказов к ID
            List<CustomerEntity> customers = repository.getAllCustomers();
            
            System.out.println("Добавлено клиентов: " + customers.size());
            
            // Навешиваем тестовые заказы на каждого клиента
            // Клиент 1 - Иванов (5 заказов)
            repository.saveOrder(customers.get(0).getId(), 1500.00, "2026-05-01", "Электроника");
            repository.saveOrder(customers.get(0).getId(), 2300.50, "2026-05-03", "Одежда");
            repository.saveOrder(customers.get(0).getId(), 890.00, "2026-05-05", "Книги");
            repository.saveOrder(customers.get(0).getId(), 4500.00, "2026-05-07", "Электроника");
            repository.saveOrder(customers.get(0).getId(), 1200.00, "2026-05-08", "Продукты");
            
            // Клиент 2 - Смирнова (4 заказа)
            repository.saveOrder(customers.get(1).getId(), 3200.00, "2026-05-02", "Электроника");
            repository.saveOrder(customers.get(1).getId(), 1500.75, "2026-05-04", "Дом и сад");
            repository.saveOrder(customers.get(1).getId(), 890.00, "2026-05-06", "Одежда");
            repository.saveOrder(customers.get(1).getId(), 2100.00, "2026-05-07", "Книги");
            
            // Клиент 3 - Козлов (3 заказа)
            repository.saveOrder(customers.get(2).getId(), 4500.00, "2026-05-01", "Электроника");
            repository.saveOrder(customers.get(2).getId(), 3200.00, "2026-05-03", "Электроника");
            repository.saveOrder(customers.get(2).getId(), 1800.00, "2026-05-05", "Одежда");
            
            // Клиент 4 - Новикова (5 заказов)
            repository.saveOrder(customers.get(3).getId(), 2700.00, "2026-05-02", "Дом и сад");
            repository.saveOrder(customers.get(3).getId(), 1500.00, "2026-05-04", "Продукты");
            repository.saveOrder(customers.get(3).getId(), 3400.00, "2026-05-06", "Электроника");
            repository.saveOrder(customers.get(3).getId(), 950.00, "2026-05-07", "Книги");
            repository.saveOrder(customers.get(3).getId(), 2200.00, "2026-05-08", "Одежда");
            
            // Клиент 5 - Волков (2 заказа)
            repository.saveOrder(customers.get(4).getId(), 5600.00, "2026-05-01", "Электроника");
            repository.saveOrder(customers.get(4).getId(), 2300.00, "2026-05-03", "Дом и сад");
            
            // Клиент 6 - Лебедева (4 заказа)
            repository.saveOrder(customers.get(5).getId(), 1800.00, "2026-05-02", "Одежда");
            repository.saveOrder(customers.get(5).getId(), 3100.00, "2026-05-04", "Электроника");
            repository.saveOrder(customers.get(5).getId(), 750.00, "2026-05-06", "Продукты");
            repository.saveOrder(customers.get(5).getId(), 1200.00, "2026-05-07", "Книги");
            
            // Клиент 7 - Соколов (4 заказа)
            repository.saveOrder(customers.get(6).getId(), 4200.00, "2026-05-01", "Электроника");
            repository.saveOrder(customers.get(6).getId(), 1900.00, "2026-05-03", "Одежда");
            repository.saveOrder(customers.get(6).getId(), 2800.00, "2026-05-05", "Дом и сад");
            repository.saveOrder(customers.get(6).getId(), 1100.00, "2026-05-07", "Продукты");
            
            // Клиент 8 - Кузнецова (4 заказа)
            repository.saveOrder(customers.get(7).getId(), 3500.00, "2026-05-02", "Электроника");
            repository.saveOrder(customers.get(7).getId(), 2100.00, "2026-05-04", "Одежда");
            repository.saveOrder(customers.get(7).getId(), 1600.00, "2026-05-06", "Книги");
            repository.saveOrder(customers.get(7).getId(), 2900.00, "2026-05-08", "Дом и сад");

            System.out.println("Main: Тестовые данные добавлены.");
            System.out.println("Всего клиентов: " + customers.size());
            
            // Подсчет общего числа заказов для лога
            int totalOrders = 0;
            for (CustomerEntity c : customers) {
                totalOrders += repository.getOrdersByCustomer(c.getId()).size();
            }
            System.out.println("Всего заказов: " + totalOrders);
        }

        System.out.println("Main: Запуск графического интерфейса...");
        // Запуск Swing-интерфейса в потоке диспетчера событий (EDT)
        OrderManagementGui.start(repository, converter);
    }
}