namespace CarFactoryApp
{
    partial class Form2
    {
        private System.ComponentModel.IContainer components = null;

        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows Form Designer generated code

        private void InitializeComponent()
        {
            this.comboBoxManufacturer = new System.Windows.Forms.ComboBox();
            this.dataGridView1 = new System.Windows.Forms.DataGridView();
            this.btnGoToPattern = new System.Windows.Forms.Button();
            this.btnExit = new System.Windows.Forms.Button();
            this.groupBoxNavigation = new System.Windows.Forms.GroupBox();
            this.label2 = new System.Windows.Forms.Label();
            this.label1 = new System.Windows.Forms.Label();
            this.labelTitle = new System.Windows.Forms.Label();
            ((System.ComponentModel.ISupportInitialize)(this.dataGridView1)).BeginInit();
            this.groupBoxNavigation.SuspendLayout();
            this.SuspendLayout();
            // 
            // comboBoxManufacturer
            // 
            this.comboBoxManufacturer.BackColor = System.Drawing.SystemColors.Info;
            this.comboBoxManufacturer.DropDownStyle = System.Windows.Forms.ComboBoxStyle.DropDownList;
            this.comboBoxManufacturer.Font = new System.Drawing.Font("Microsoft Sans Serif", 9F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(204)));
            this.comboBoxManufacturer.FormattingEnabled = true;
            this.comboBoxManufacturer.Location = new System.Drawing.Point(3, 47);
            this.comboBoxManufacturer.Margin = new System.Windows.Forms.Padding(4);
            this.comboBoxManufacturer.Name = "comboBoxManufacturer";
            this.comboBoxManufacturer.Size = new System.Drawing.Size(332, 26);
            this.comboBoxManufacturer.TabIndex = 0;
            this.comboBoxManufacturer.SelectedIndexChanged += new System.EventHandler(this.comboBoxManufacturer_SelectedIndexChanged);
            // 
            // dataGridView1
            // 
            this.dataGridView1.AllowUserToAddRows = false;
            this.dataGridView1.AllowUserToDeleteRows = false;
            this.dataGridView1.ColumnHeadersHeightSizeMode = System.Windows.Forms.DataGridViewColumnHeadersHeightSizeMode.AutoSize;
            this.dataGridView1.Location = new System.Drawing.Point(3, 81);
            this.dataGridView1.Margin = new System.Windows.Forms.Padding(4);
            this.dataGridView1.Name = "dataGridView1";
            this.dataGridView1.ReadOnly = true;
            this.dataGridView1.RowHeadersWidth = 51;
            this.dataGridView1.Size = new System.Drawing.Size(501, 300);
            this.dataGridView1.TabIndex = 1;
            // 
            // btnGoToPattern
            // 
            this.btnGoToPattern.Location = new System.Drawing.Point(11, 39);
            this.btnGoToPattern.Margin = new System.Windows.Forms.Padding(4);
            this.btnGoToPattern.Name = "btnGoToPattern";
            this.btnGoToPattern.Size = new System.Drawing.Size(187, 37);
            this.btnGoToPattern.TabIndex = 2;
            this.btnGoToPattern.Text = "← С паттерном";
            this.btnGoToPattern.UseVisualStyleBackColor = true;
            this.btnGoToPattern.Click += new System.EventHandler(this.btnGoToPattern_Click);
            // 
            // btnExit
            // 
            this.btnExit.Location = new System.Drawing.Point(11, 111);
            this.btnExit.Margin = new System.Windows.Forms.Padding(4);
            this.btnExit.Name = "btnExit";
            this.btnExit.Size = new System.Drawing.Size(187, 37);
            this.btnExit.TabIndex = 3;
            this.btnExit.Text = "Выход";
            this.btnExit.UseVisualStyleBackColor = true;
            this.btnExit.Click += new System.EventHandler(this.btnExit_Click);
            // 
            // groupBoxNavigation
            // 
            this.groupBoxNavigation.Controls.Add(this.btnExit);
            this.groupBoxNavigation.Controls.Add(this.btnGoToPattern);
            this.groupBoxNavigation.Controls.Add(this.label2);
            this.groupBoxNavigation.Controls.Add(this.label1);
            this.groupBoxNavigation.Location = new System.Drawing.Point(512, 15);
            this.groupBoxNavigation.Margin = new System.Windows.Forms.Padding(4);
            this.groupBoxNavigation.Name = "groupBoxNavigation";
            this.groupBoxNavigation.Padding = new System.Windows.Forms.Padding(4);
            this.groupBoxNavigation.Size = new System.Drawing.Size(208, 163);
            this.groupBoxNavigation.TabIndex = 4;
            this.groupBoxNavigation.TabStop = false;
            this.groupBoxNavigation.Text = "Навигация";
            // 
            // label2
            // 
            this.label2.AutoSize = true;
            this.label2.Location = new System.Drawing.Point(8, 80);
            this.label2.Margin = new System.Windows.Forms.Padding(4, 0, 4, 0);
            this.label2.Name = "label2";
            this.label2.Size = new System.Drawing.Size(146, 16);
            this.label2.TabIndex = 1;
            this.label2.Text = "Закрыть приложение";
            // 
            // label1
            // 
            this.label1.AutoSize = true;
            this.label1.Location = new System.Drawing.Point(8, 19);
            this.label1.Margin = new System.Windows.Forms.Padding(4, 0, 4, 0);
            this.label1.Name = "label1";
            this.label1.Size = new System.Drawing.Size(133, 16);
            this.label1.TabIndex = 0;
            this.label1.Text = "Вернуться к форме";
            // 
            // labelTitle
            // 
            this.labelTitle.AutoSize = true;
            this.labelTitle.Font = new System.Drawing.Font("Microsoft Sans Serif", 12F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(204)));
            this.labelTitle.ForeColor = System.Drawing.Color.FromArgb(((int)(((byte)(192)))), ((int)(((byte)(0)))), ((int)(((byte)(192)))));
            this.labelTitle.Location = new System.Drawing.Point(-2, 9);
            this.labelTitle.Margin = new System.Windows.Forms.Padding(4, 0, 4, 0);
            this.labelTitle.Name = "labelTitle";
            this.labelTitle.Size = new System.Drawing.Size(421, 25);
            this.labelTitle.TabIndex = 5;
            this.labelTitle.Text = "🚗 Сборка автомобилей (БЕЗ паттерна)";
            // 
            // Form2
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(8F, 16F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(747, 394);
            this.Controls.Add(this.labelTitle);
            this.Controls.Add(this.groupBoxNavigation);
            this.Controls.Add(this.dataGridView1);
            this.Controls.Add(this.comboBoxManufacturer);
            this.FormBorderStyle = System.Windows.Forms.FormBorderStyle.FixedSingle;
            this.Margin = new System.Windows.Forms.Padding(4);
            this.MaximizeBox = false;
            this.Name = "Form2";
            this.StartPosition = System.Windows.Forms.FormStartPosition.CenterScreen;
            this.Text = "Form2";
            this.FormClosing += new System.Windows.Forms.FormClosingEventHandler(this.Form2_FormClosing);
            ((System.ComponentModel.ISupportInitialize)(this.dataGridView1)).EndInit();
            this.groupBoxNavigation.ResumeLayout(false);
            this.groupBoxNavigation.PerformLayout();
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.ComboBox comboBoxManufacturer;
        private System.Windows.Forms.DataGridView dataGridView1;
        private System.Windows.Forms.Button btnGoToPattern;
        private System.Windows.Forms.Button btnExit;
        private System.Windows.Forms.GroupBox groupBoxNavigation;
        private System.Windows.Forms.Label label1;
        private System.Windows.Forms.Label label2;
        private System.Windows.Forms.Label labelTitle;
    }
}