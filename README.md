# Student Service Hub – Campus Service Management System

## Project Description
Student Service Hub is a backend-driven campus service management platform developed using **Java, Spring Boot, Hibernate (JPA), and MySQL**. The system centralizes essential student services such as **stationery ordering, bus tracking, and reminder management** into a single integrated platform.

The project focuses mainly on **Java backend development and database-driven application design**, demonstrating how modern backend technologies can be used to manage institutional services efficiently. The system follows a layered architecture consisting of **controllers, services, repositories, and database entities**, ensuring scalability and maintainability.

Student Service Hub integrates multiple campus services into one platform. Students can browse and order stationery items, track buses with ETA notifications, and schedule reminders for academic or personal tasks. Administrators can manage inventory, transport data, and system notifications through dedicated dashboards.

With features such as **real-time GPS logging, automated reminders, transactional order management, and role-based access control**, the project demonstrates practical implementation of **DBMS concepts, RESTful APIs, and enterprise-style backend architecture using Spring Boot**.

---

# 📱 Features

## Student Features

### Stationery Ordering
- Browse available stationery items
- View item prices and stock availability
- Place digital orders
- Generate QR codes for order pickup

### Bus Tracking System
- View bus routes and stops
- Real-time bus location updates via GPS logs
- ETA (Estimated Time of Arrival) calculation for stops

### Reminder Management
- Create reminders for assignments, exams, or events
- Automated notifications triggered at scheduled times

### Notifications & Alerts
- Alerts for bus arrival
- Reminder notifications
- Email notifications for important updates

### Profile Management
- Secure login system
- Personalized dashboard with orders and reminders

---

## Admin Features

### Stationery Management
- Add new stationery items
- Update item prices and stock
- Manage student orders

### Bus Administration
- Add and manage buses
- Define bus routes and stops
- Monitor GPS logs and transport data

### Reminder & Notification Control
- Manage system notifications
- Monitor reminder triggers

### Database Monitoring
- View and manage system records such as orders, reminders, and buses

---

# 🏗️ Architecture

```
studentservicehub/
├── src/main/java/com/studenthub/studentservicehub
│   ├── controller/
│   │   ├── BusController.java
│   │   ├── BusAdminController.java
│   │   ├── StationeryController.java
│   │   ├── StationeryAdminController.java
│   │   ├── ReminderController.java
│   │   └── MainController.java
│
│   ├── service/
│   │   ├── GPSManager.java
│   │   ├── NotificationService.java
│   │   ├── EmailService.java
│   │   └── OrderService.java
│
│   ├── repository/
│   │   ├── BusRepository.java
│   │   ├── StopRepository.java
│   │   ├── GpsLogRepository.java
│   │   ├── ItemRepository.java
│   │   ├── OrderRepository.java
│   │   └── StudentRepository.java
│
│   ├── model/
│   │   ├── Bus.java
│   │   ├── Stop.java
│   │   ├── GpsLog.java
│   │   ├── Item.java
│   │   ├── Order.java
│   │   ├── Payment.java
│   │   ├── Reminder.java
│   │   ├── Student.java
│   │   └── Notification.java
│
├── src/main/resources/
│   └── application.properties
│
├── pom.xml
└── StudentservicehubApplication.java
```

---

# 🗄️ Database Schema

The system uses a **MySQL relational database** with normalized tables.

### Main Tables

- `students` → Stores student user data  
- `items` → Stationery items and stock information  
- `orders` → Orders placed by students  
- `payments` → Payment details for orders  
- `buses` → Bus route and driver details  
- `stops` → Bus stop locations  
- `gps_logs` → Bus GPS location updates  
- `reminders` → Student reminder schedules  
- `notifications` → System notification records  

### Relationships

- Student → Orders (**One-to-Many**)  
- Student → Reminders (**One-to-Many**)  
- Bus → Stops (**One-to-Many**)  
- Order → Item (**Many-to-One**)  

Primary and foreign keys maintain **data integrity and relational consistency**.

---

# 🚀 Getting Started

## Prerequisites

- Java 17 or later
- Maven
- MySQL Server
- IntelliJ IDEA / Eclipse
- Postman (optional for API testing)

---

## Installation

### Clone the Repository

```bash
git clone https://github.com/your-username/student-service-hub.git
cd student-service-hub
```

### Install Dependencies

```bash
mvn clean install
```

---

# ⚙️ Database Setup

Create a MySQL database:

```sql
CREATE DATABASE studentservicehub;
```

Update database credentials in:

```
src/main/resources/application.properties
```

Example configuration:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/studentservicehub
spring.datasource.username=root
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
```

---

# ▶️ Run the Application

Run using Maven:

```bash
mvn spring-boot:run
```

Or run the main class:

```
StudentservicehubApplication.java
```

---

# 🔐 Security

### Role-Based Access Control

**Student Role**
- Place stationery orders
- Track buses
- Create reminders

**Admin Role**
- Manage items, buses, and stops
- Monitor system records

### Security Measures

- Password hashing
- Prepared statements to prevent SQL injection
- Secure API access

---

# 📦 Key Dependencies

```
spring-boot-starter-web
spring-boot-starter-data-jpa
mysql-connector-java
spring-boot-starter-mail
spring-boot-starter-thymeleaf
spring-boot-starter-test
hibernate-core
```

---

# 🛠️ Build Commands

```bash
mvn clean package      # Build project
mvn spring-boot:run    # Run application
mvn test               # Run tests
```

---

# 📊 System Highlights

- Backend-focused architecture using **Spring Boot**
- Scalable **service-layer design**
- **MySQL relational schema** with normalized tables
- Automated reminder scheduling
- Real-time bus tracking with ETA calculation
- Email notification system
- REST-based backend architecture

---

