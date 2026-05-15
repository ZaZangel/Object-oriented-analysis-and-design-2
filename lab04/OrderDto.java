public class OrderDto { // DTO для отображения в таблице заказов
    
    private String date;      // Дата заказа
    private double amount;    // Сумма (уже округленная конвертером)
    private String category;  // Категория

    public OrderDto(String date, double amount, String category) {
        this.date = date;
        this.amount = amount;
        this.category = category;
    }

    public String getDate() { return date; }
    public double getAmount() { return amount; }
    public String getCategory() { return category; }
}