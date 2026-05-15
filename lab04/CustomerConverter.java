import java.util.List; // Импорт интерфейса List для работы со списками заказов

public class CustomerConverter { // Объявление класса-преобразователя. Это ядро паттерна Converter
    
    // Метод преобразования "сырой" сущности клиента в DTO для интерфейса
    // Принимает клиента и список всех его заказов для агрегации данных
    public CustomerDto convertToDto(CustomerEntity customer, List<OrderEntity> orders) { 
        // Вычисление общей суммы покупок через Stream API: берет amount из каждого заказа и суммирует
        double totalSpent = orders.stream().mapToDouble(OrderEntity::getAmount).sum(); 
        // Получение количества заказов через размер переданного списка
        int orderCount = orders.size(); 
        
        // Расчет среднего чека с защитой от деления на ноль (если заказов нет)
        double averageOrderValue = orderCount == 0 ? 0 : totalSpent / orderCount; 
        // Округление среднего чека до 2 знаков после запятой (денежный формат)
        averageOrderValue = Math.round(averageOrderValue * 100.0) / 100.0; 
        // Округление общей суммы также до 2 знаков
        totalSpent = Math.round(totalSpent * 100.0) / 100.0;

        // Создание и возврат нового DTO, содержащего только нужные для UI поля + рассчитанные метрики
        return new CustomerDto(
            customer.getId(),      // ID из Entity
            customer.getName(),    // Имя из Entity
            customer.getEmail(),   // Email из Entity
            totalSpent,            // Рассчитанная общая сумма
            orderCount,            // Рассчитанное количество заказов
            averageOrderValue      // Рассчитанный средний чек
        );
    }

    // Метод преобразования одного заказа в DTO для таблицы заказов
    public OrderDto convertToOrderDto(OrderEntity order) { 
        // Возврат нового DTO заказа с округленной суммой
        return new OrderDto(
            order.getDate(),                              // Дата из Entity
            Math.round(order.getAmount() * 100.0) / 100.0, // Сумма с округлением
            order.getCategory()                           // Категория из Entity
        );
    }
}