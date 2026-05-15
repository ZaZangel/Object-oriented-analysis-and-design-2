import java.util.List;

public class MainNoPattern {
    public static void main(String[] args) {
        CustomerRepository repository = new CustomerRepository();

        List<CustomerEntity> existingCustomers = repository.getAllCustomers();
        
        if (existingCustomers.isEmpty()) {
            System.out.println("Main: База пуста. Генерируем тестовые данные...");
            
            repository.saveCustomer("Иванов Петр Сергеевич", "ivanov@email.ru", "+7-900-123-45-67");
            repository.saveCustomer("Смирнова Анна Владимировна", "smirnova@email.ru", "+7-900-765-43-21");
            repository.saveCustomer("Козлов Дмитрий Александрович", "kozlov@email.ru", "+7-900-111-22-33");
            repository.saveCustomer("Новикова Елена Игоревна", "novikova@email.ru", "+7-900-444-55-66");
            repository.saveCustomer("Волков Максим Олегович", "volkov@email.ru", "+7-900-777-88-99");
            repository.saveCustomer("Лебедева Ольга Павловна", "lebedeva@email.ru", "+7-900-222-33-44");
            repository.saveCustomer("Соколов Артём Дмитриевич", "sokolov@email.ru", "+7-900-555-66-77");
            repository.saveCustomer("Кузнецова Марина Сергеевна", "kuznetsova@email.ru", "+7-900-888-99-00");

            List<CustomerEntity> customers = repository.getAllCustomers();
            
            System.out.println("Добавлено клиентов: " + customers.size());
            
            // Иванов
            repository.saveOrder(customers.get(0).getId(), 1500.00, "2026-05-01", "Электроника");
            repository.saveOrder(customers.get(0).getId(), 2300.50, "2026-05-03", "Одежда");
            repository.saveOrder(customers.get(0).getId(), 890.00, "2026-05-05", "Книги");
            repository.saveOrder(customers.get(0).getId(), 4500.00, "2026-05-07", "Электроника");
            repository.saveOrder(customers.get(0).getId(), 1200.00, "2026-05-08", "Продукты");
            
            // Смирнова
            repository.saveOrder(customers.get(1).getId(), 3200.00, "2026-05-02", "Электроника");
            repository.saveOrder(customers.get(1).getId(), 1500.75, "2026-05-04", "Дом и сад");
            repository.saveOrder(customers.get(1).getId(), 890.00, "2026-05-06", "Одежда");
            repository.saveOrder(customers.get(1).getId(), 2100.00, "2026-05-07", "Книги");
            
            // Козлов
            repository.saveOrder(customers.get(2).getId(), 4500.00, "2026-05-01", "Электроника");
            repository.saveOrder(customers.get(2).getId(), 3200.00, "2026-05-03", "Электроника");
            repository.saveOrder(customers.get(2).getId(), 1800.00, "2026-05-05", "Одежда");
            
            // Новикова
            repository.saveOrder(customers.get(3).getId(), 2700.00, "2026-05-02", "Дом и сад");
            repository.saveOrder(customers.get(3).getId(), 1500.00, "2026-05-04", "Продукты");
            repository.saveOrder(customers.get(3).getId(), 3400.00, "2026-05-06", "Электроника");
            repository.saveOrder(customers.get(3).getId(), 950.00, "2026-05-07", "Книги");
            repository.saveOrder(customers.get(3).getId(), 2200.00, "2026-05-08", "Одежда");
            
            // Волков
            repository.saveOrder(customers.get(4).getId(), 5600.00, "2026-05-01", "Электроника");
            repository.saveOrder(customers.get(4).getId(), 2300.00, "2026-05-03", "Дом и сад");
            
            // Лебедева
            repository.saveOrder(customers.get(5).getId(), 1800.00, "2026-05-02", "Одежда");
            repository.saveOrder(customers.get(5).getId(), 3100.00, "2026-05-04", "Электроника");
            repository.saveOrder(customers.get(5).getId(), 750.00, "2026-05-06", "Продукты");
            repository.saveOrder(customers.get(5).getId(), 1200.00, "2026-05-07", "Книги");
            
            // Соколов
            repository.saveOrder(customers.get(6).getId(), 4200.00, "2026-05-01", "Электроника");
            repository.saveOrder(customers.get(6).getId(), 1900.00, "2026-05-03", "Одежда");
            repository.saveOrder(customers.get(6).getId(), 2800.00, "2026-05-05", "Дом и сад");
            repository.saveOrder(customers.get(6).getId(), 1100.00, "2026-05-07", "Продукты");
            
            // Кузнецова
            repository.saveOrder(customers.get(7).getId(), 3500.00, "2026-05-02", "Электроника");
            repository.saveOrder(customers.get(7).getId(), 2100.00, "2026-05-04", "Одежда");
            repository.saveOrder(customers.get(7).getId(), 1600.00, "2026-05-06", "Книги");
            repository.saveOrder(customers.get(7).getId(), 2900.00, "2026-05-08", "Дом и сад");

            System.out.println("Main: Тестовые данные добавлены.");
            System.out.println("Всего клиентов: " + customers.size());
            
            int totalOrders = 0;
            for (CustomerEntity c : customers) {
                totalOrders += repository.getOrdersByCustomer(c.getId()).size();
            }
            System.out.println("Всего заказов: " + totalOrders);
        }

        System.out.println("Main: Запуск графического интерфейса (БЕЗ ПАТТЕРНА)...");
        OrderManagementGuiNoPattern.start(repository);
    }
}