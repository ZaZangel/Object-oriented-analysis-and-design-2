public class CustomerDto { // Data Transfer Object: облегченная модель для передачи в GUI
    
    private Long id;                // ID клиента
    private String name;            // Отображаемое имя
    private String email;           // Контактный email
    private double totalSpent;      // Агрегированное поле: сумма всех покупок
    private int orderCount;         // Агрегированное поле: количество заказов
    private double averageOrderValue; // Агрегированное поле: средний чек

    // Конструктор DTO принимает уже рассчитанные значения
    public CustomerDto(Long id, String name, String email, double totalSpent, 
                       int orderCount, double averageOrderValue) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.totalSpent = totalSpent;
        this.orderCount = orderCount;
        this.averageOrderValue = averageOrderValue;
    }

    // Геттеры используются JTable для заполнения строк
    public String getName() { return name; }
    public String getEmail() { return email; }
    public double getTotalSpent() { return totalSpent; }
    public int getOrderCount() { return orderCount; }
    public double getAverageOrderValue() { return averageOrderValue; }
}