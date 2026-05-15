import com.formdev.flatlaf.FlatLightLaf; // Импорт библиотеки для современной темы оформления
import javax.swing.*;                    // Импорт компонентов Swing
import javax.swing.table.DefaultTableModel; // Импорт модели данных для JTable
import java.awt.*;                       // Импорт менеджеров компоновки
import java.util.List;                   // Импорт коллекций

public class OrderManagementGui extends JFrame { // Основное окно приложения
    private CustomerRepository repository; // Ссылка на слой данных
    private CustomerConverter converter;   // Ссылка на слой преобразования
    private JTable customerTable;          // Таблица клиентов
    private DefaultTableModel customerModel; // Модель данных таблицы клиентов
    private JTable orderTable;             // Таблица заказов
    private DefaultTableModel orderModel;  // Модель данных таблицы заказов

    // Конструктор окна
    public OrderManagementGui(CustomerRepository repo, CustomerConverter conv) {
        this.repository = repo;  // Внедрение зависимости репозитория
        this.converter = conv;   // Внедрение зависимости конвертера

        setTitle("Система управления заказами клиентов"); // Заголовок окна
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);   // Действие при закрытии
        setSize(1000, 650);        // Размеры окна
        setLocationRelativeTo(null); // Центрирование на экране

        JTabbedPane tabs = new JTabbedPane(); // Создание вкладок
        tabs.addTab("Клиенты", createCustomerPanel()); // Добавление панели клиентов
        tabs.addTab("Заказы", createOrderPanel());     // Добавление панели заказов
        add(tabs); // Размещение вкладок в окне

        updateCustomerTable(null); // Первичная загрузка данных
    }

    // Метод создания панели управления клиентами
    private JPanel createCustomerPanel() {
        JPanel panel = new JPanel(new BorderLayout()); // Панель с BorderLayout

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT)); // Панель кнопок
        JTextField searchField = new JTextField(15); // Поле поиска
        JButton searchBtn = new JButton("Поиск");    // Кнопка поиска
        JButton addBtn = new JButton("Добавить клиента"); // Кнопка добавления
        JButton editBtn = new JButton("Редактировать");   // Кнопка редактирования
        JButton deleteBtn = new JButton("Удалить");       // Кнопка удаления

        toolbar.add(new JLabel("Имя:"));
        toolbar.add(searchField);
        toolbar.add(searchBtn);
        toolbar.add(new JSeparator(SwingConstants.VERTICAL)); // Разделитель
        toolbar.add(addBtn);
        toolbar.add(editBtn);
        toolbar.add(deleteBtn);

        // Создание модели таблицы с заголовками колонок
        customerModel = new DefaultTableModel(
            new String[]{"ID", "Имя", "Email", "Всего потрачено", "Заказов", "Средний чек"}, 0);
        customerTable = new JTable(customerModel); // Создание компонента таблицы
        panel.add(toolbar, BorderLayout.NORTH);    // Размещение тулбара сверху
        panel.add(new JScrollPane(customerTable), BorderLayout.CENTER); // Таблица в скролле

        // Привязка обработчиков событий к кнопкам
        searchBtn.addActionListener(e -> updateCustomerTable(searchField.getText()));
        addBtn.addActionListener(e -> showAddCustomerDialog());
        deleteBtn.addActionListener(e -> deleteSelectedCustomer());
        editBtn.addActionListener(e -> showEditCustomerDialog());

        return panel;
    }

    // Метод создания панели заказов
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

    // Метод обновления таблицы клиентов (фильтрация или полная загрузка)
    private void updateCustomerTable(String filter) {
        customerModel.setRowCount(0); // Очистка текущих строк
        // Получение списка Entity из репозитория
        List<CustomerEntity> customers = (filter == null || filter.isEmpty()) 
            ? repository.getAllCustomers() : repository.searchCustomers(filter);

        // Проход по каждому клиенту
        for (CustomerEntity c : customers) {
            List<OrderEntity> orders = repository.getOrdersByCustomer(c.getId());
            // !!! КЛЮЧЕВОЙ МОМЕНТ ПАТТЕРНА: вызов конвертера для преобразования Entity -> DTO
            CustomerDto dto = converter.convertToDto(c, orders);
            // Добавление строки в таблицу из готового DTO
            customerModel.addRow(new Object[]{
                c.getId(), dto.getName(), dto.getEmail(), 
                dto.getTotalSpent() + " ₽", dto.getOrderCount(), dto.getAverageOrderValue() + " ₽"
            });
        }
    }

    // Диалог добавления нового клиента
    private void showAddCustomerDialog() {
        JTextField name = new JTextField();
        JTextField email = new JTextField();
        JTextField phone = new JTextField();
        Object[] message = {"Имя:", name, "Email:", email, "Телефон:", phone};

        int option = JOptionPane.showConfirmDialog(null, message, "Новый клиент", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            repository.saveCustomer(name.getText(), email.getText(), phone.getText());
            updateCustomerTable(null); // Перерисовка таблицы
        }
    }

    // Диалог редактирования выбранного клиента
    private void showEditCustomerDialog() {
        int row = customerTable.getSelectedRow();
        if (row == -1) return; // Выход, если строка не выбрана
        
        long id = (long) customerTable.getValueAt(row, 0);
        String currentName = (String) customerTable.getValueAt(row, 1);
        String currentEmail = (String) customerTable.getValueAt(row, 2);
        String currentPhone = (String) customerTable.getValueAt(row, 3);

        JTextField nameField = new JTextField(currentName);
        JTextField emailField = new JTextField(currentEmail);
        JTextField phoneField = new JTextField(currentPhone);
        Object[] message = {"Имя:", nameField, "Email:", emailField, "Телефон:", phoneField};

        int option = JOptionPane.showConfirmDialog(null, message, "Редактировать", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            repository.updateCustomer(id, nameField.getText(), emailField.getText(), phoneField.getText());
            updateCustomerTable(null);
        }
    }

    // Удаление выбранного клиента
    private void deleteSelectedCustomer() {
        int row = customerTable.getSelectedRow();
        if (row != -1) {
            long id = (long) customerTable.getValueAt(row, 0);
            int confirm = JOptionPane.showConfirmDialog(null, "Удалить клиента и все его заказы?", "Подтверждение", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                repository.deleteCustomer(id);
                updateCustomerTable(null);
                orderModel.setRowCount(0); // Очистка таблицы заказов
            }
        }
    }

    // Обновление таблицы заказов для выбранного клиента
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
            // Преобразование заказа в DTO перед выводом
            OrderDto dto = converter.convertToOrderDto(o);
            orderModel.addRow(new Object[]{dto.getDate(), dto.getAmount() + " ₽", dto.getCategory()});
        }
    }

    // Диалог добавления нового заказа
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

        int result = JOptionPane.showConfirmDialog(null, message, "Добавить заказ", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
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
                JOptionPane.showMessageDialog(this, "Заказ успешно добавлен!", "Успех", JOptionPane.INFORMATION_MESSAGE);
                updateCustomerTable(null);
                updateOrderTable();
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Некорректная сумма заказа!", "Ошибка", JOptionPane.ERROR_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Ошибка: " + e.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    // Заглушка удаления заказа
    private void deleteSelectedOrder() {
        int row = orderTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Выберите заказ для удаления", "Внимание", JOptionPane.WARNING_MESSAGE);
            return;
        }
        JOptionPane.showMessageDialog(this, "Удаление заказов через GUI не реализовано", "Информация", JOptionPane.INFORMATION_MESSAGE);
    }

    // Статический метод запуска GUI в потоке EDT (стандарт Swing)
    public static void start(CustomerRepository repo, CustomerConverter conv) {
        EventQueue.invokeLater(() -> {
            FlatLightLaf.setup(); // Применение темы оформления
            new OrderManagementGui(repo, conv).setVisible(true); // Создание и показ окна
        });
    }
}