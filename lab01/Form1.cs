using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Windows.Forms;

namespace CarFactoryApp
{
    public partial class Form1 : Form
    {
        private BindingList<CarPartRow> partsList = new BindingList<CarPartRow>();
        private CarFactory factory;

        public class CarPartRow
        {
            public string Component { get; set; }
            public string Specification { get; set; }
        }

        // ═══════════════════════════════════════════════════════
        // АБСТРАКТНЫЕ ПРОДУКТЫ
        // ═══════════════════════════════════════════════════════

        abstract class Body
        {
            public abstract string Type { get; }
            public abstract string Material { get; }
            public abstract string Color { get; }
        }

        abstract class Engine
        {
            public abstract string Type { get; }
            public abstract int Horsepower { get; }
            public abstract string FuelType { get; }
        }

        abstract class Transmission
        {
            public abstract string Type { get; }
            public abstract int Gears { get; }
        }

        abstract class Wheels
        {
            public abstract string Brand { get; }
            public abstract int Diameter { get; }
            public abstract string TireType { get; }
        }

        // ═══════════════════════════════════════════════════════
        // КОНКРЕТНЫЕ ПРОДУКТЫ - BMW
        // ═══════════════════════════════════════════════════════

        class BMWBody : Body
        {
            public override string Type => "Седан M-Sport";
            public override string Material => "Алюминиевый сплав";
            public override string Color => "Alpine White";
        }

        class BMWEngine : Engine
        {
            public override string Type => "B58 Turbo Inline-6";
            public override int Horsepower => 382;
            public override string FuelType => "Бензин";
        }

        class BMWTransmission : Transmission
        {
            public override string Type => "ZF 8-ступенчатый автомат";
            public override int Gears => 8;
        }

        class BMWWheels : Wheels
        {
            public override string Brand => "BMW M Performance";
            public override int Diameter => 19;
            public override string TireType => "Michelin Pilot Sport 4S";
        }

        // ═══════════════════════════════════════════════════════
        // КОНКРЕТНЫЕ ПРОДУКТЫ - Mercedes
        // ═══════════════════════════════════════════════════════

        class MercedesBody : Body
        {
            public override string Type => "C-Class AMG Line";
            public override string Material => "Сталь с алюминием";
            public override string Color => "Obsidian Black";
        }

        class MercedesEngine : Engine
        {
            public override string Type => "M256 Turbo Inline-6";
            public override int Horsepower => 362;
            public override string FuelType => "Бензин + EQ Boost";
        }

        class MercedesTransmission : Transmission
        {
            public override string Type => "9G-TRONIC";
            public override int Gears => 9;
        }

        class MercedesWheels : Wheels
        {
            public override string Brand => "AMG Multispoke";
            public override int Diameter => 19;
            public override string TireType => "Pirelli P Zero";
        }

        // ═══════════════════════════════════════════════════════
        // КОНКРЕТНЫЕ ПРОДУКТЫ - Audi
        // ═══════════════════════════════════════════════════════

        class AudiBody : Body
        {
            public override string Type => "A6 Sedan S-Line";
            public override string Material => "Алюминиевый каркас";
            public override string Color => "Daytona Gray";
        }

        class AudiEngine : Engine
        {
            public override string Type => "3.0 TFSI V6";
            public override int Horsepower => 340;
            public override string FuelType => "Бензин";
        }

        class AudiTransmission : Transmission
        {
            public override string Type => "S tronic (DSG)";
            public override int Gears => 7;
        }

        class AudiWheels : Wheels
        {
            public override string Brand => "Audi Sport";
            public override int Diameter => 20;
            public override string TireType => "Continental SportContact";
        }

        // ═══════════════════════════════════════════════════════
        // АБСТРАКТНАЯ ФАБРИКА
        // ═══════════════════════════════════════════════════════

        abstract class CarFactory
        {
            public abstract Body CreateBody();
            public abstract Engine CreateEngine();
            public abstract Transmission CreateTransmission();
            public abstract Wheels CreateWheels();
            public abstract string ManufacturerName { get; }
        }

        // ═══════════════════════════════════════════════════════
        // КОНКРЕТНЫЕ ФАБРИКИ
        // ═══════════════════════════════════════════════════════

        class BMWFactory : CarFactory
        {
            public override string ManufacturerName => "BMW";
            public override Body CreateBody() => new BMWBody();
            public override Engine CreateEngine() => new BMWEngine();
            public override Transmission CreateTransmission() => new BMWTransmission();
            public override Wheels CreateWheels() => new BMWWheels();
        }

        class MercedesFactory : CarFactory
        {
            public override string ManufacturerName => "Mercedes-Benz";
            public override Body CreateBody() => new MercedesBody();
            public override Engine CreateEngine() => new MercedesEngine();
            public override Transmission CreateTransmission() => new MercedesTransmission();
            public override Wheels CreateWheels() => new MercedesWheels();
        }

        class AudiFactory : CarFactory
        {
            public override string ManufacturerName => "Audi";
            public override Body CreateBody() => new AudiBody();
            public override Engine CreateEngine() => new AudiEngine();
            public override Transmission CreateTransmission() => new AudiTransmission();
            public override Wheels CreateWheels() => new AudiWheels();
        }

        // ═══════════════════════════════════════════════════════
        // ФОРМА
        // ═══════════════════════════════════════════════════════

        public Form1()
        {
            InitializeComponent();
            SetupForm();
        }

        private void SetupForm()
        {
            this.Text = "Сборка автомобилей (С паттерном Abstract Factory)";

            // Настройка DataGridView
            dataGridView1.AutoGenerateColumns = false;
            dataGridView1.AutoSizeColumnsMode = DataGridViewAutoSizeColumnsMode.Fill;
            dataGridView1.Columns.Clear();
            dataGridView1.Columns.Add("Component", "Компонент");
            dataGridView1.Columns["Component"].DataPropertyName = "Component";
            dataGridView1.Columns.Add("Specification", "Характеристика");
            dataGridView1.Columns["Specification"].DataPropertyName = "Specification";
            dataGridView1.DataSource = partsList;

            // Заполнение ComboBox
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

            // Создаём нужную фабрику
            if (brand == "BMW")
                factory = new BMWFactory();
            else if (brand == "Mercedes-Benz")
                factory = new MercedesFactory();
            else if (brand == "Audi")
                factory = new AudiFactory();
            else
                return;

            // Собираем автомобиль через фабрику
            Body body = factory.CreateBody();
            Engine engine = factory.CreateEngine();
            Transmission transmission = factory.CreateTransmission();
            Wheels wheels = factory.CreateWheels();

            // Добавляем в таблицу
            partsList.Add(new CarPartRow { Component = "🚙 Кузов", Specification = $"{body.Type}, {body.Material}, {body.Color}" });
            partsList.Add(new CarPartRow { Component = "⚙️ Двигатель", Specification = $"{engine.Type}, {engine.Horsepower} л.с., {engine.FuelType}" });
            partsList.Add(new CarPartRow { Component = "🔄 КПП", Specification = $"{transmission.Type}, {transmission.Gears} передач" });
            partsList.Add(new CarPartRow { Component = "🛞 Колёса", Specification = $"{wheels.Brand}, {wheels.Diameter}\", {wheels.TireType}" });
        }

        private void btnGoToNoPattern_Click(object sender, EventArgs e)
        {
            Form2 form2 = new Form2();
            form2.Show();
            this.Hide();
        }

        private void btnExit_Click(object sender, EventArgs e)
        {
            Application.Exit();
        }

        private void Form1_FormClosing(object sender, FormClosingEventArgs e)
        {
            Application.Exit();
        }
    }
}