# 🚀 Stock-Pulse (Global Inventory & Notification System)

[![License](https://img.shields.io/badge/License-Apache_2.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x_/_4.x-green.svg)](https://spring.io/projects/spring-boot)
[![Docker](https://img.shields.io/badge/Docker-Enabled-blue.svg)](https://www.docker.com/)

**Stock-Pulse** is an enterprise-grade Global Inventory & Warehouse Management backend system designed for high-concurrency environments. It manages distributed products, inventories, and warehouses while enforcing strict transactional integrity, real-time alerting, and robust security.

---

## 🛠️ Advanced Architectural Features

This application implements several industry-standard practices, making it highly secure, fault-tolerant, and performant:

### 1. 🔐 Security & Identity Control
*   **Asymmetric JWT Signatures**: Uses public/private RSA-2048 key pairs loaded from externalized environment variables to sign and verify JSON Web Tokens (JWT).
*   **Role-Based Access Control (RBAC)**: Secure authorization mappings for `ADMIN`, `SELLER`, and `BUYER` roles.
*   **Privilege Escalation Protection**: Ensures default registrations strictly assign the `BUYER` role, rejecting unauthorized requests to create `ADMIN` accounts.

### 2. ⚡ Concurrency Control (Race Condition Prevention)
To eliminate lost updates and race conditions (e.g., two buyers purchasing the last item at the exact same millisecond):
*   **Optimistic Locking**: Implemented via JPA `@Version` on the `Inventory` entity to detect and reject outdated updates.
*   **Pessimistic Locking**: Configured `@Lock(LockModeType.PESSIMISTIC_WRITE)` (`SELECT FOR UPDATE`) on critical database operations (like stock reduction and transfers) to serialize writes on hot rows under heavy traffic.

### 3. 📧 Non-Blocking Asynchronous Notifications
*   **AOP Proxy Bypass Resolved**: Separated notification logic into a dedicated `NotificationService` to ensure Spring AOP proxies execute the `@Async` annotation correctly on a background thread pool.
*   **Dynamic Mail Routing**: Automatically resolves the contact email of the specific warehouse manager where a low-stock event occurred, utilizing externalized configurations and fallback addresses.

### 4. 📐 JSR-380 Payload Validation & Global Exception Mapping
*   **Strict Validation**: Enforces payload checks on controllers using `@Valid` combined with JSR-380 validation annotations (`@NotBlank`, `@Min`, `@Email`, `@Size`), rejecting malformed payloads before hitting the database.
*   **Standardized REST Error Payloads**: Implements `@RestControllerAdvice` to intercept exceptions (e.g., `InsufficientStockException`, `ResourceNotFoundException`) and translate them into consistent client-friendly JSON error payloads.

---

## 🏗️ System Architecture & Data Flow

```mermaid
sequenceDiagram
    autonumber
    Client ->> Controller: PUT /inventory/sell?productId=...&warehouseId=...&qty=6
    Note over Controller: Checks JWT token validity & role permissions
    Controller ->> InventoryService: sellProduct(productId, warehouseId, qty)
    Note over InventoryService: Fetches Inventory with Pessimistic Write Lock
    Note over InventoryService: Verifies optimistic @Version matches
    Note over InventoryService: Subtracts stock in MySQL database
    alt Stock falls below Product threshold
        InventoryService ->>> NotificationService: triggerLowStockAlert(inventory)
        Note over NotificationService: Spawns background task (Asynchronous)
        NotificationService -->> Database: Saves alert in Notification table (unread)
        NotificationService -->> SMTP (Mailtrap): Connects and sends Alert Email
    end
    InventoryService ->> Database: Saves audit log to Transaction table
    InventoryService -->> Controller: Returns updated Inventory entity
    Controller -->> Client: 200 OK (Instant response)
```

---

## 🗄️ Database Schema

The database model is composed of the following entities:
*   `Product`: Item specifications and low-stock threshold settings.
*   `WareHouse`: Physical facilities with localized settings and manager contacts.
*   `Inventory`: Relational entity linking Products to Warehouses, with a version counter for optimistic locking.
*   `Transaction`: Complete ledger recording all inbound, outbound, and transfer operations.
*   `Notification`: Log of all triggered low-stock alerts.
*   `User`: Authentication database mapping users to roles.

---

## 🚀 Getting Started

### 📋 Environment Variables
The application reads its production configuration from environment variables. Create a local `.env` file (which is ignored by Git) with these keys:

```bash
# Database Settings
DB_URL=jdbc:mysql://<your-db-host>:<port>/<db-name>
DB_USERNAME=<your-username>
DB_PASSWORD=<your-password>

# SMTP Server Settings (e.g., Mailtrap / Gmail)
SMTP_USERNAME=<your-smtp-username>
SMTP_PASSWORD=<your-smtp-password>

# JWT Security Signature Keys
RSA_PUBLIC_KEY=<your-rsa-public-key-pem>
RSA_PRIVATE_KEY=<your-rsa-private-key-pem>
```

### 📦 Local Execution with Maven
1.  Generate your RSA public and private keys using OpenSSL and save them in the environment.
2.  Start your local database and Redis server.
3.  Run the application using:
    ```bash
    mvn spring-boot:run
    ```

---

## 🔗 API Documentation
Swagger UI auto-documentation is available locally at:
```text
http://localhost:10000/docs
```

---

## 🧪 Running Tests
Isolated unit and integration tests run on an in-memory H2 database:
```bash
./mvnw clean test
```

---

## 👨‍💻 Author
**Mahek Patel**  
GitHub: [Mahekpatel06](https://github.com/Mahekpatel06)  
License: Apache 2.0
