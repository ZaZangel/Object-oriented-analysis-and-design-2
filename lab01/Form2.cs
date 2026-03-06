using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Windows.Forms;

namespace CarFactoryApp
{
    public partial class Form2 : Form
    {
        // BindingList автоматически обновляет таблицу при изменении данных
        private BindingList<CarPartRow> partsList = new BindingList<CarPartRow>();

        // Модель данных для строки таблицы
        public class CarPartRow
        {
            public string Component { get; set; }
            public string Specification { get; set; }
        }

        // Простой класс-контейнер для данных (без абстракций — ключевое отличие от паттерна!)
        class CarComponent
        {
            public string Name { get; set; }
            public string Specs { get; set; }
        }

        public Form2()
        {
            InitializeComponent();
            SetupForm();
        }

        private void SetupForm()
        {
            this.Text = "Сборка автомобилей (БЕЗ паттерна)";

            // Настройка таблицы: ручное управление колонками + привязка к данным
            dataGridView1.AutoGenerateColumns = false;
            dataGridView1.AutoSizeColumnsMode = DataGridViewAutoSizeColumnsMode.Fill;
            dataGridView1.Columns.Clear();
            dataGridView1.Columns.Add("Component", "Компонент");
            dataGridView1.Columns["Component"].DataPropertyName = "Component";
            dataGridView1.Columns.Add("Specification", "Характеристика");
            dataGridView1.Columns["Specification"].DataPropertyName = "Specification";
            dataGridView1.DataSource = partsList; // Data Binding: таблица обновляется автоматически

            // Заполнение списка производителей
            comboBoxManufacturer.Items.Clear();
            comboBoxManufacturer.Items.Add("BMW");
            comboBoxManufacturer.Items.Add("Mercedes-Benz");
            comboBoxManufacturer.Items.Add("Audi");
            comboBoxManufacturer.Text = "Выберите производителя";
        }

        private void comboBoxManufacturer_SelectedIndexChanged(object sender, EventArgs e)
        {
            partsList.Clear();
            string brand = comboBoxManufacturer.SelectedItem.ToString();

            // ❗ БЕЗ ПАТТЕРНА: создание компонентов через if/else
            // Проблема: для добавления нового бренда нужно МЕНЯТЬ этот код
            CarComponent body, engine, transmission, wheels;

            if (brand == "BMW")
            {
                body = new CarComponent { Name = "Кузов", Specs = "Седан M-Sport, Алюминиевый сплав, Alpine White" };
                engine = new CarComponent { Name = "Двигатель", Specs = "B58 Turbo Inline-6, 382 л.с., Бензин" };
                transmission = new CarComponent { Name = "КПП", Specs = "ZF 8-ступенчатый автомат, 8 передач" };
                wheels = new CarComponent { Name = "Колёса", Specs = "BMW M Performance, 19\", Michelin Pilot Sport 4S" };
            }
            else if (brand == "Mercedes-Benz")
            {
                body = new CarComponent { Name = "Кузов", Specs = "C-Class AMG Line, Сталь с алюминием, Obsidian Black" };
                engine = new CarComponent { Name = "Двигатель", Specs = "M256 Turbo Inline-6, 362 л.с., Бензин + EQ Boost" };
                transmission = new CarComponent { Name = "КПП", Specs = "9G-TRONIC, 9 передач" };
                wheels = new CarComponent { Name = "Колёса", Specs = "AMG Multispoke, 19\", Pirelli P Zero" };
            }
            else // Audi
            {
                body = new CarComponent { Name = "Кузов", Specs = "A6 Sedan S-Line, Алюминиевый каркас, Daytona Gray" };
                engine = new CarComponent { Name = "Двигатель", Specs = "3.0 TFSI V6, 340 л.с., Бензин" };
                transmission = new CarComponent { Name = "КПП", Specs = "S tronic (DSG), 7 передач" };
                wheels = new CarComponent { Name = "Колёса", Specs = "Audi Sport, 20\", Continental SportContact" };
            }

            // Добавление данных в таблицу (обновление происходит автоматически благодаря BindingList)
            partsList.Add(new CarPartRow { Component = $"🚙 {body.Name}", Specification = body.Specs });
            partsList.Add(new CarPartRow { Component = $"⚙️ {engine.Name}", Specification = engine.Specs });
            partsList.Add(new CarPartRow { Component = $"🔄 {transmission.Name}", Specification = transmission.Specs });
            partsList.Add(new CarPartRow { Component = $"🛞 {wheels.Name}", Specification = wheels.Specs });
        }

        private void btnGoToPattern_Click(object sender, EventArgs e)
        {
            Form1 form1 = new Form1();
            form1.Show();
            this.Hide();
        }

        private void btnExit_Click(object sender, EventArgs e)
        {
            Application.Exit();
        }

        private void Form2_FormClosing(object sender, FormClosingEventArgs e)
        {
            Application.Exit();
        }
    }
}