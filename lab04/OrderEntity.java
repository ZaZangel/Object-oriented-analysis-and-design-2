public class OrderEntity { // Класс-сущность, отражает таблицу orders в БД
    
    private Long id;            // ID заказа
    private Long customerId;    // Внешний ключ, связывающий заказ с клиентом
    private double amount;      // Сумма заказа
    private String date;        // Дата совершения
    private String category;    // Категория товара/услуги

    // Конструктор для маппинга данных из ResultSet
    public OrderEntity(Long id, Long customerId, double amount, String date, String category) {
        this.id = id;
        this.customerId = customerId;
        this.amount = amount;
        this.date = date;
        this.category = category;
    }

    // Геттеры только для чтения (в рамках паттерна данные заказа не меняются через UI)
    public Long getCustomerId() { return customerId; }
    public double getAmount() { return amount; }
    public String getDate() { return date; }
    public String getCategory() { return category; }
}