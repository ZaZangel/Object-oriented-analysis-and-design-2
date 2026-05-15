public class CustomerEntity { // Класс-сущность, точно повторяет структуру таблицы customers в БД
    
    // Поля, соответствующие колонкам таблицы БД
    private Long id;        // Первичный ключ
    private String name;    // Имя клиента
    private String email;   // Электронная почта
    private String phone;   // Номер телефона

    // Конструктор для создания объекта при чтении из ResultSet
    public CustomerEntity(Long id, String name, String email, String phone) {
        this.id = id;       // Инициализация поля id
        this.name = name;   // Инициализация поля name
        this.email = email; // Инициализация поля email
        this.phone = phone; // Инициализация поля phone
    }

    // Геттеры для безопасного доступа к полям (инкапсуляция)
    public Long getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    
    // Сеттеры для изменения данных перед сохранением в БД
    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setPhone(String phone) { this.phone = phone; }
}