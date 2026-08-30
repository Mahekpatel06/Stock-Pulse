# 🚀 GINS (Global Inventory & Notification System)

[![License](https://img.shields.io/badge/License-Apache_2.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.x-green.svg)](https://spring.io/projects/spring-boot)
[![Docker](https://img.shields.io/badge/Docker-Enabled-blue.svg)](https://www.docker.com/)

**GINS** (Stock-Pulse) is an enterprise-grade backend system designed for high-concurrency inventory, product, and warehouse management. It features transactional integrity, non-blocking real-time notifications, dynamic routing, and asymmetric JWT authorization.

---

## 💡 Core Concepts & Architecture Explained

Here is how GINS manages complex backend tasks in an understandable, plain-English way:

### 1. 🔐 Asymmetric JWT Security (Security & Identity Gate)
* **What it does:** GINS protects its APIs using JSON Web Tokens (JWT). When a user registers or logs in, the server generates a cryptographically signed token.
* **Why it's secure:** It uses **Asymmetric RSA-2048 key pairs** (a private key and a public key). The server signs the token using the private key, and verifies it using the public key. The private key never leaves the secure server environment.
* **Roles & Permissions:** Users are assigned roles (`BUYER`, `SELLER`, `ADMIN`). Certain endpoints (like adding stock or transferring items) are locked to `ADMIN` and `SELLER` only.
* **Privilege Escalation Protection:** The system defaults all new registrations to the `BUYER` role. Even if a client requests an `ADMIN` role during registration, it is overridden and saved as `BUYER` to prevent security breaches.

### 2. ⚡ Race Condition Prevention (Concurrency Control)
When thousands of buyers try to purchase the same hot product at the exact same millisecond, standard databases can suffer from "lost updates" (selling items that are out of stock). GINS solves this using two mechanisms:
* **Pessimistic Locking:** When a sale/transfer request comes in, the server uses `@Lock(LockModeType.PESSIMISTIC_WRITE)`. This tells the database to execute a `SELECT ... FOR UPDATE`, locking the inventory row. Any other requests trying to edit the same row are forced to wait in line until the transaction finishes.
* **Optimistic Locking:** GINS also includes JPA `@Version` tracking on the `Inventory` entity. If two requests somehow bypass the lock, the version mismatch is detected, and the database automatically rolls back the second transaction to prevent corrupted data.

### 3. 📧 Non-Blocking Asynchronous Notifications
* **What it does:** If product stock falls below the threshold, GINS alerts the warehouse manager.
* **Non-Blocking Execution:** Sending an email takes time (1-3 seconds). To prevent the user's screen from lagging, the system spawns the alert on a separate thread pool (`@Async`), allowing the purchase request to finish instantly while the email is sent in the background.
* **Dynamic Routing:** Instead of hardcoded support emails, GINS dynamically queries the specific warehouse manager's contact details to route the email dynamically.

### 4. 📐 Payload Validation & Exception Mapping
* **Validations:** Every request payload is checked using Spring validations (e.g., ensuring quantity is not negative, email is valid, names are not blank) before the system accesses the database.
* **Custom Error Handling:** If a transaction fails (e.g., `InsufficientStockException`), a global error advisor (`@RestControllerAdvice`) converts the Java exception stack trace into a clean, client-friendly JSON response.

---

## 🗄️ Database Entities Overview

* `Product`: Specifications, pricing, and safety low-stock threshold settings.
* `WareHouse`: Physical facilities containing contact details for dynamic alerts.
* `Inventory`: Maps products to warehouses with specific stock counts and optimistic `@Version` counters.
* `Transaction`: Logs every movement (Inbound, Outbound/Sale, and Transfers between warehouses).
* `Notification`: History logs of triggered low-stock alerts.
* `User`: Credentials, password hashes, and authorization roles.

---

## 🚀 Getting Started & Local Setup

If you have forked this repository, follow these steps to run the project on your machine.

### 📋 Prerequisites
Make sure you have the following installed:
* **Java 21** or higher
* **Maven 3.x**
* **MySQL Database**
* **Redis Server** (optional, used for caching/timeouts)

---

### 🔑 Step 1: RSA Key Generation
JWT tokens require RSA-2048 private and public keys. You can generate them using `openssl` in your terminal:

```bash
# Generate private key
openssl genrsa -out keypair.pem 2048

# Extract public key from the keypair
openssl rsa -in keypair.pem -pubout -out public.pem
```
For local testing, you can also locate ready-to-use keys inside the project at `src/main/resources/certs/private.pem` and `public.pem`.

---

### ⚙️ Step 2: Environment Variables
Create a `.env` file in the root folder of the project (this file is ignored by Git to protect your secrets). Add the following variables:

```bash
# Database connection settings
DB_URL=jdbc:mysql://localhost:3306/gins_db?createDatabaseIfNotExist=true
DB_USERNAME=your_mysql_username
DB_PASSWORD=your_mysql_password

# Asymmetric RSA keys in PEM format (paste the raw text of public.pem & private.pem)
RSA_PUBLIC_KEY="-----BEGIN PUBLIC KEY-----\nMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA...\n-----END PUBLIC KEY-----"
RSA_PRIVATE_KEY="-----BEGIN PRIVATE KEY-----\nMIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEA...\n-----END PRIVATE KEY-----"

# SMTP Settings for Low-Stock Notifications (e.g. Mailtrap)
SMTP_USERNAME=your_smtp_username
SMTP_PASSWORD=your_smtp_password
```

---

### 📦 Step 3: Run the Application

#### Option A: Running with Maven (Local Dev)
1. Ensure your local MySQL and Redis servers are running.
2. In the project root, run:
   ```bash
   mvn spring-boot:run
   ```

#### Option B: Running with Docker Compose (Containerized Dev)
This is the easiest way to launch the app and its database dependencies without manual installation.
Run:
```bash
docker-compose up --build
```
The app will start on port `10000`.

---

## 🧪 Running Tests
To run unit and integration tests (which run in-memory using an H2 database):
```bash
mvn clean test
```

---

## 🌐 Trying the APIs

Once GINS is running, you can interact with all APIs using the built-in **Swagger UI** or via Client tools (like Postman or cURL).

### 📖 Swagger UI
Open your browser and navigate to:
```text
http://localhost:10000/docs
```

### 🛠️ Step-by-Step API Walkthrough

To try the full flow, perform the following API calls in Swagger or Postman:

#### 1. Register a User
Send a `POST` request to `/register` with a username and password. This will default to the `BUYER` role. If you want to perform administrative tasks, register with `SELLER`.
* **Endpoint:** `POST http://localhost:10000/register`
* **Request Body:**
  ```json
  {
    "name": "john_doe",
    "password": "securepassword123",
    "role": "SELLER"
  }
  ```

#### 2. Log in and retrieve the JWT Token
Authenticate with the registered credentials to receive your security token.
* **Endpoint:** `POST http://localhost:10000/login`
* **Request Body:**
  ```json
  {
    "name": "john_doe",
    "password": "securepassword123"
  }
  ```
* **Response:**
  ```json
  {
    "token": "eyJhbGciOiJSUzI1NiIsIn..."
  }
  ```

#### 3. Authorize your Swagger UI / Client Requests
* In Swagger UI, click the **"Authorize"** button at the top right.
* Type `Bearer <paste-your-token-here>` into the input box and click Authorize.
* For Postman or cURL, add the header: `Authorization: Bearer <your-jwt-token>`.

#### 4. Try Core Operations (Requires ADMIN/SELLER Token)
* **Create a Warehouse:**
  `POST /warehouses` with layout configuration.
* **Create a Product:**
  `POST /products` with safety threshold configurations.
* **Inbound / Add Stock:**
  `POST /inventory/add` to stock a product in a warehouse.
* **Sell Product (Simulate purchases):**
  `PUT /inventory/sell?productId=<id>&warehouseId=<id>&qty=<amount>`
* **Transfer Stock:**
  `POST /inventory/transfer?productId=<id>&fromWhId=<id>&toWhId=<id>&qty=<amount>`
* **View Transactions / Notifications:**
  `GET /transactions` and `GET /notifications` to review stock movements and generated alerts.

---

## 👨‍💻 Author
**Mahek Patel**  
GitHub: [@Mahekpatel06](https://github.com/Mahekpatel06)  
License: Apache 2.0
