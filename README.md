# 🚀 Stock-Pulse

**Enterprise-grade Global Inventory & Warehouse Management System**

Stock-Pulse is a robust backend system built using Spring Boot that enables efficient inventory tracking, warehouse management, and real-time stock notifications. It simplifies stock operations like adding, selling, and transferring products across global warehouses.

---

## 📌 📖 Overview

This project solves the problem of **manual inventory tracking and delayed stock awareness** by:

* Managing products and warehouses globally 🌍
* Tracking real-time inventory levels 📦
* Sending **low-stock email alerts** 📧
* Providing secure APIs with JWT authentication 🔐

---

## 🛠️ ⚙️ Tech Stack

* **Java**: 21
* **Framework**: Spring Boot
* **Build Tool**: Maven
* **Database**: MySQL (Hosted on Aiven Cloud)
* **Security**: JWT Authentication (RSA Key Pair using OpenSSL)
* **API Docs**: Swagger (OpenAPI 3.1)
* **Email Service**: Mailtrap (for testing alerts)
* **Architecture**:

  * Spring Data JPA (JpaRepository)
  * DTO Pattern
  * HATEOAS for REST responses

---

## ✨ 🚀 Features

### 🔐 Authentication

* User Registration (`/register`)
* User Login (`/login`)
* Role-based access (SELLER, BUYER)
* Admin role managed manually (secured)

### 📦 Inventory & Warehouse Management

* Add stock, sell products, and transfer stock between warehouses
* Search inventory by product category or warehouse location
* View all products in a warehouse, or all warehouses holding a product

### 💰 Transactions

* Track all stock movements

---

## 🗄️ 🧩 Database Design

Entities used in the system:

* `Product`
* `Warehouse`
* `Inventory`
* `Notification`
* `Transaction`
* `User`

---

## 🔗 📡 API Documentation

Swagger UI available at: 
```
https://stock-pulse-q8tw.onrender.com
```

---

## 📧 🔔 Email Notification System

* Triggered when stock < threshold
* Uses Mailtrap (for safe email testing)
* Example recipient: Warehouse Manager

---

## 🔐 🔑 Security Implementation

* JWT-based authentication
* RSA Public/Private key encryption
* Tokens required for protected APIs

---

#### ▶️ Quick Start: Test JWT APIs

Use the following request body for both **Register** and **Login** APIs:

```json
{
  "name": "yourname",
  "password": "yourpassword",
  "role": "SELLER"
}
```

**Step 1: Register User**

```
POST /register
```

**Step 2: Login to Get JWT Token**

```
POST /login
```

👉 The login response will return a **JWT token**.

**Step 3: Use Token in Swagger / APIs**

Add the token in the Authorize header:

```
Authorization: <your_token>
```

Now you can access secured APIs based on your role.

---

## 👩‍💻 Author

**Mahek Patel**
GitHub: ``` https://github.com/Mahekpatel06 ```

---

## 📜 📄 License

This project is licensed under **Apache 2.0**

---
