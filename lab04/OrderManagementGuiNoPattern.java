import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * ГРАФИЧЕСКИЙ ИНТЕРФЕЙС БЕЗ ПАТТЕРНА CONVERTER
 * 
 * ОТЛИЧИЯ ОТ ВЕРСИИ С ПАТТЕРНОМ:
 * 1. НЕТ поля `converter` — все расчеты внутри этого класса
 * 2. Конструктор принимает ТОЛЬКО repository (нет converter)
 * 3. ЕСТЬ методы calculateTotalSpent, calculateAverageOrderValue и т.д.
 * 4. В updateCustomerTable() расчеты выполняются прямо в цикле
 * 5. Нарушен принцип единственной ответственности (SRP): GUI и считает, и рисует
 */
public class OrderManagementGuiNoPattern extends JFrame {
    
    private CustomerRepository repository;
    
    //  ОТЛИЧИЕ: НЕТ поля converter! Все расчеты делаются внутри этого класса
    
    private JTable customerTable;
    private DefaultTableModel customerModel;
    private JTable orderTable;
    private DefaultTableModel orderModel;

    /**
     *  ОТЛИЧИЕ: Конструктор принимает ТОЛЬКО репозиторий
     * В версии с паттерном конструктор принимает ещё и converter
     */
    public OrderManagementGuiNoPattern(CustomerRepository repo) {
        this.repository = repo;
        //  ОТЛИЧИЕ: Нет this.converter = conv;

        setTitle("Система управления заказами клиентов (БЕЗ ПАТТЕРНА)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 650);
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Клиенты", createCustomerPanel());
        tabs.addTab("Заказы", createOrderPanel());
        add(tabs);

        updateCustomerTable(null);
    }

    //  КЛЮЧЕВОЕ ОТЛИЧИЕ: Этих методов НЕТ в версии с паттерном! 
    // Вся бизнес-логика (расчеты) находится прямо здесь, в GUI-классе
    
    /**
     * Расчет общей суммы покупок клиента
     *  В версии с паттерном этот код находится в CustomerConverter.convertToDto()
     */
    private double calculateTotalSpent(List<OrderEntity> orders) {
        double total = 0;
        for (OrderEntity order : orders) {
            total += order.getAmount(); // Суммируем все заказы
        }
        return Math.round(total * 100.0) / 100.0; // Округляем до 2 знаков
    }
    
    /**
     * Подсчет количества заказов
     *  В версии с паттерном это делается внутри конвертера
     */
    private int calculateOrderCount(List<OrderEntity> orders) {
        return orders.size();
    }
    
    /**
     * Расчет среднего чека (средней суммы заказа)
     *  В версии с паттерном эта логика изолирована в CustomerConverter
     */
    private double calculateAverageOrderValue(List<OrderEntity> orders) {
        if (orders.isEmpty()) return 0; // Защита от деления на ноль
        double total = calculateTotalSpent(orders); // Повторный вызов (неэффективно!)
        double average = total / orders.size();
        return Math.round(average * 100.0) / 100.0;
    }
    
    /**
     * Вспомогательный метод округления
     * В версии с паттерном округление делается в DTO или Converter
     */
    private double roundAmount(double amount) {
        return Math.round(amount * 100.0) / 100.0;
    }
    //  КОНЕЦ БЛОКА РАСЧЕТОВ 

    // Методы редактирования/добавления/удаления — одинаковы в обеих версиях
    private void showEditCustomerDialog() {
        int row = customerTable.getSelectedRow();
        if (row == -1) return;
        
        long id = (long) customerTable.getValueAt(row, 0);
        String currentName = (String) customerTable.getValueAt(row, 1);
        String currentEmail = (String) customerTable.getValueAt(row, 2);
        String currentPhone = (String) customerTable.getValueAt(row, 3);

        JTextField nameField = new JTextField(currentName);
        JTextField emailField = new JTextField(currentEmail);
        JTextField phoneField = new JTextField(currentPhone);
        Object[] message = {"Имя:", nameField, "Email:", emailField, "Телефон:", phoneField};

        int option = JOptionPane.showConfirmDialog(null, message, "Редактировать клиента", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            repository.updateCustomer(id, nameField.getText(), emailField.getText(), phoneField.getText());
            updateCustomerTable(null);
        }
    }

    private JPanel createCustomerPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField searchField = new JTextField(15);
        JButton searchBtn = new JButton("Поиск");
        JButton addBtn = new JButton("Добавить клиента");
        JButton editBtn = new JButton("Редактировать");
        JButton deleteBtn = new JButton("Удалить");

        toolbar.add(new JLabel("Имя:"));
        toolbar.add(searchField);
        toolbar.add(searchBtn);
        toolbar.add(new JSeparator(SwingConstants.VERTICAL));
        toolbar.add(addBtn);
        toolbar.add(editBtn);
        toolbar.add(deleteBtn);

        customerModel = new DefaultTableModel(
            new String[]{"ID", "Имя", "Email", "Всего потрачено", "Заказов", "Средний чек"}, 0);
        customerTable = new JTable(customerModel);
        panel.add(toolbar, BorderLayout.NORTH);
        panel.add(new JScrollPane(customerTable), BorderLayout.CENTER);

        searchBtn.addActionListener(e -> updateCustomerTable(searchField.getText()));
        addBtn.addActionListener(e -> showAddCustomerDialog());
        deleteBtn.addActionListener(e -> deleteSelectedCustomer());
        editBtn.addActionListener(e -> showEditCustomerDialog());

        return panel;
    }

    private JPanel createOrderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        orderModel = new DefaultTableModel(new String[]{"Дата", "Сумма", "Категория"}, 0);
        orderTable = new JTable(orderModel);
        
        JButton viewOrdersBtn = new JButton("Показать заказы выбранного клиента");
        JButton addOrderBtn = new JButton("Добавить заказ");
        JButton deleteOrderBtn = new JButton("Удалить заказ");
        
        JPanel toolbar = new JPanel();
        toolbar.add(viewOrdersBtn);
        toolbar.add(addOrderBtn);
        toolbar.add(deleteOrderBtn);

        panel.add(toolbar, BorderLayout.NORTH);
        panel.add(new JScrollPane(orderTable), BorderLayout.CENTER);

        viewOrdersBtn.addActionListener(e -> updateOrderTable());
        addOrderBtn.addActionListener(e -> showAddOrderDialog());
        deleteOrderBtn.addActionListener(e -> deleteSelectedOrder());

        return panel;
    }

    /**
     *  САМОЕ ВАЖНОЕ ОТЛИЧИЕ: Метод обновления таблицы клиентов 
     * 
     * БЕЗ ПАТТЕРНА (этот код):
     * - Сам считает totalSpent, orderCount, averageOrderValue
     * - Вызывает методы calculateTotalSpent() прямо внутри цикла отрисовки
     * - Работает напрямую с Entity
     * - Нарушает SRP: GUI занимается и отображением, и бизнес-логикой
     * 
     * С ПАТТЕРНОМ (в другой версии):
     * - Вызывает converter.convertToDto() и получает готовые данные
     * - Не содержит никакой математики
     * - Работает с DTO
     */
    private void updateCustomerTable(String filter) {
        customerModel.setRowCount(0);
        List<CustomerEntity> customers = (filter == null || filter.isEmpty()) 
            ? repository.getAllCustomers() : repository.searchCustomers(filter);

        for (CustomerEntity c : customers) {
            List<OrderEntity> orders = repository.getOrdersByCustomer(c.getId());
            
            //  ОТЛИЧИЕ: Расчеты выполняются ПРЯМО ЗДЕСЬ, в GUI
            // В версии с паттерном эти строки заменены на:
            // CustomerDto dto = converter.convertToDto(c, orders);
            double totalSpent = calculateTotalSpent(orders);           //  Считаем сами
            int orderCount = calculateOrderCount(orders);              //  Считаем сами
            double averageOrderValue = calculateAverageOrderValue(orders); // Считаем сами
            
            // Заполняем таблицу локальными переменными (не из DTO)
            customerModel.addRow(new Object[]{
                c.getId(), 
                c.getName(), 
                c.getEmail(), 
                totalSpent + " ₽",              //  Переменная, а не поле DTO
                orderCount,                     //  Переменная, а не поле DTO
                averageOrderValue + " ₽"        //  Переменная, а не поле DTO
            });
        }
    }

    private void showAddCustomerDialog() {
        JTextField name = new JTextField();
        JTextField email = new JTextField();
        JTextField phone = new JTextField();
        Object[] message = {"Имя:", name, "Email:", email, "Телефон:", phone};

        int option = JOptionPane.showConfirmDialog(null, message, "Новый клиент", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            repository.saveCustomer(name.getText(), email.getText(), phone.getText());
            updateCustomerTable(null);
        }
    }

    private void deleteSelectedCustomer() {
        int row = customerTable.getSelectedRow();
        if (row != -1) {
            long id = (long) customerTable.getValueAt(row, 0);
            int confirm = JOptionPane.showConfirmDialog(null, "Удалить клиента и все его заказы?", "Подтверждение", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                repository.deleteCustomer(id);
                updateCustomerTable(null);
                orderModel.setRowCount(0);
            }
        }
    }

    /**
     *  ОТЛИЧИЕ: Обновление таблицы заказов
     * 
     * БЕЗ ПАТТЕРНА: Работает напрямую с OrderEntity, округляет на месте
     * С ПАТТЕРНОМ: Использует converter.convertToOrderDto()
     */
    private void updateOrderTable() {
        int row = customerTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Выберите клиента во вкладке 'Клиенты'");
            return;
        }
        long customerId = (long) customerTable.getValueAt(row, 0);
        orderModel.setRowCount(0);
        List<OrderEntity> orders = repository.getOrdersByCustomer(customerId);
        
        for (OrderEntity o : orders) {
            //  ОТЛИЧИЕ: Прямая работа с Entity + округление здесь
            orderModel.addRow(new Object[]{
                o.getDate(), 
                roundAmount(o.getAmount()) + " ₽", //  Округляем прямо при отрисовке
                o.getCategory()
            });
        }
    }

    private void showAddOrderDialog() {
        int row = customerTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Сначала выберите клиента в таблице!", "Внимание", JOptionPane.WARNING_MESSAGE);
            return;
        }
        long customerId = (long) customerTable.getValueAt(row, 0);
        String customerName = (String) customerTable.getValueAt(row, 1);

        JTextField amount = new JTextField();
        JTextField date = new JTextField("2026-05-08");
        JComboBox<String> categoryCombo = new JComboBox<>(new String[]{"Электроника", "Одежда", "Книги", "Дом и сад", "Продукты", "Другое"});
        Object[] message = {"Клиент: " + customerName, "Сумма заказа (₽):", amount, "Дата (ГГГГ-ММ-ДД):", date, "Категория:", categoryCombo};

        int result = JOptionPane.showConfirmDialog(null, message, "Добавить заказ для " + customerName, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            try {
                String amountText = amount.getText().trim();
                if (amountText.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Введите сумму заказа!", "Ошибка", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                double orderAmount = Double.parseDouble(amountText);
                if (orderAmount <= 0) {
                    JOptionPane.showMessageDialog(this, "Сумма должна быть больше 0!", "Ошибка", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                String orderDate = date.getText().trim();
                if (orderDate.isEmpty()) orderDate = "2026-05-08";
                
                repository.saveOrder(customerId, orderAmount, orderDate, (String) categoryCombo.getSelectedItem());
                JOptionPane.showMessageDialog(this, "Заказ на сумму " + orderAmount + " ₽ успешно добавлен!", "Успех", JOptionPane.INFORMATION_MESSAGE);
                updateCustomerTable(null);
                updateOrderTable();
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Некорректная сумма заказа! Введите число.", "Ошибка", JOptionPane.ERROR_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Ошибка: " + e.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    private void deleteSelectedOrder() {
        int row = orderTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Выберите заказ для удаления", "Внимание", JOptionPane.WARNING_MESSAGE);
            return;
        }
        JOptionPane.showMessageDialog(this, "Удаление заказов через GUI не реализовано", "Информация", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * ОТЛИЧИЕ: Метод запуска принимает ТОЛЬКО repository
     * В версии с паттерном метод принимает ещё и converter
     */
    public static void start(CustomerRepository repo) {
        EventQueue.invokeLater(() -> {
            FlatLightLaf.setup();
            new OrderManagementGuiNoPattern(repo).setVisible(true);
        });
    }
}