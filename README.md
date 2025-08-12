# Mazenta Hospital Management System

A comprehensive desktop application for managing hospital operations including appointments, laboratory tests, pharmacy management, and administrative tasks.

## 🎥 Demo
Watch the system in action: [YouTube Demo](https://youtu.be/9cUsQg7drEE?si=nTQOkUaPUNoKmWSc)

## 🚀 Features

### Core Modules
- **Appointment Management** - Schedule and manage doctor appointments
- **Laboratory Test Management** - Handle test requests and generate reports
- **Pharmacy Management** - Manage inventory, billing, and stock monitoring
- **User Management** - Role-based access control for different user types

### User Roles
- **Super Admin** - Full system access, user registration
- **Admin** - System management (except admin registration)
- **Receptionist** - Appointments, lab tests, payments
- **Laboratorist** - Generate test reports
- **Pharmacist** - Complete pharmacy operations
- **Pharmacy Stock Manager** - Inventory and supplier management
- **Pharmacy Cashier** - Sales and billing

## 🛠️ Technology Stack

- **Language:** Java (JDK 8)
- **GUI Framework:** Java Swing
- **Database:** MySQL
- **Report Generation:** JasperReports
- **IDE:** NetBeans 8.1

## 📋 System Requirements

### Hardware
- RAM: 2GB min
- Storage: 100MB free space
- Processor: Intel Core i3 or equivalent

### Software
- Windows 7/8/10/11
- JRE 8
- MySQL Server

## ⚡ Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/JaniduChamika/Mazenta
   ```

2. Install MySQL Server and create database named `mazenta2`

3. Configure database connection in `dist/src/model/MySQL.java`:
   ```java
   private static final String USERNAME = "root";
   private static final String PASSWORD = "your_password";
   private static final String DATABASE = "mazenta2";
   ```

4. Ensure JasperReports library is in the classpath

5. Run the application with JRE 8

## 🔐 Security Features

- Unique login credentials for each user
- Role-based access control

## 📊 Key Capabilities

- Real-time appointment scheduling
- Automated receipt and report generation
- Inventory tracking with expiry alerts
- Financial reporting and analytics
- Patient record management

## 🤝 Contributing

Feel free to submit issues and enhancement requests!

---

*Developed to digitize and streamline hospital operations, replacing manual file-based systems with an efficient computerized solution.*
