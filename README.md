<div align="center">

# 🛒 E-Commerce Backend API

### A secure, scalable, and production-ready REST API built with Spring Boot

[![Java](https://img.shields.io/badge/Java-17+-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![MySQL](https://img.shields.io/badge/MySQL-8.0+-blue.svg)](https://www.mysql.com/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

[Features](#-features) • [Tech Stack](#-tech-stack) • [Getting Started](#-getting-started) • [API Documentation](#-api-documentation) • [Architecture](#-architecture)

</div>

---

## 📋 Overview

A comprehensive backend solution for e-commerce platforms featuring complete RESTful APIs for authentication, product management, cart operations, orders, and address handling. Built with **Spring Boot** and secured with **JWT authentication**, this project follows clean architecture principles and is ready to integrate with any frontend application.

Perfect for developers looking to understand enterprise-level Spring Boot applications or build upon a solid e-commerce foundation.

---

## ✨ Features

<table>
<tr>
<td width="50%">

### 🔐 **Authentication & Security**
- ✅ JWT-based stateless authentication
- ✅ Secure user registration & login
- ✅ Role-based authorization (Admin/User)
- ✅ Cookie-based token handling
- ✅ Spring Security integration
- ✅ Password encryption with BCrypt

</td>
<td width="50%">

### 🛍️ **E-Commerce Core**
- ✅ Product CRUD operations
- ✅ Category management system
- ✅ Shopping cart functionality
- ✅ Order creation & tracking
- ✅ Multi-address support per user
- ✅ Automatic price calculations

</td>
</tr>
<tr>
<td width="50%">

### ⚙️ **Backend Capabilities**
- ✅ RESTful API design
- ✅ Layered architecture
- ✅ Global exception handling
- ✅ Request validation
- ✅ Pagination & sorting
- ✅ DTO pattern implementation

</td>
<td width="50%">

### 📊 **Database & ORM**
- ✅ MySQL database
- ✅ Hibernate ORM
- ✅ Spring Data JPA
- ✅ Entity relationships
- ✅ Transaction management
- ✅ Query optimization

</td>
</tr>
</table>

---

## 🛠 Tech Stack

```text
┌─────────────────────────────────────────────────────────────┐
│  Language        │  Java 17+                                 │
│  Framework       │  Spring Boot 3.x                          │
│  Security        │  Spring Security + JWT                    │
│  Database        │  MySQL 8.0+                               │
│  ORM             │  Hibernate + Spring Data JPA              │
│  Build Tool      │  Maven                                    │
│  Validation      │  Jakarta Bean Validation                  │
│  Testing         │  JUnit, Mockito                           │
│  API Testing     │  Postman                                  │
└─────────────────────────────────────────────────────────────┘
```

---

## 🏗 Architecture

```
com.ecommerce.EcomProj/
│
├── 📁 controller/          # REST API endpoints & request handling
├── 📁 service/             # Business logic layer
├── 📁 repository/          # Data access layer (JPA repositories)
├── 📁 model/               # Entity classes & domain models
├── 📁 payload/             # Data Transfer Objects (Request/Response)
├── 📁 security/            # JWT & Spring Security configuration
├── 📁 exception/           # Global exception handling
├── 📁 config/              # Application configuration
├── 📁 utils/               # Utility functions  
└── 📄 EcomProjApplication.java
```

### **Layered Architecture Flow**
```
Client Request → Controller → Service → Repository → Database
                     ↓           ↓          ↓
                   DTO      Business     Entity
                           Validation   Mapping
```

---

## 🚀 Getting Started

### Prerequisites

Make sure you have the following installed:

- **Java 17+** - [Download](https://www.oracle.com/java/technologies/downloads/)
- **MySQL 8.0+** - [Download](https://dev.mysql.com/downloads/)
- **Maven 3.6+** - [Download](https://maven.apache.org/download.cgi)
- **Git** - [Download](https://git-scm.com/downloads)

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/KennyAck001/Ecommerce-backend.git
   cd Ecommerce-backend
   ```

2. **Configure MySQL Database**
   ```sql
   CREATE DATABASE ecommerce_db;
   ```

3. **Set Environment Variables**

   Create a `.env` file or set system environment variables:
   ```properties
   DB_USERNAME=root
   DB_PASSWORD=your_mysql_password
   JWT_SECRET=your_secret_key_min_256_bits
   ```

4. **Update application.properties**
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/ecommerce_db
   spring.datasource.username=${DB_USERNAME}
   spring.datasource.password=${DB_PASSWORD}
   spring.app.jwtSecret=${JWT_SECRET}
   ```

5. **Build and Run**
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

6. **Verify Installation**
   ```
   🚀 Server running at: http://localhost:8080
   ```

---

## 📡 API Documentation

### Authentication Endpoints

| Method | Endpoint            | Description | Access |
|--------|---------------------|-------------|--------|
| `POST` | `/api/auth/signup`  | Register new user | Public |
| `POST` | `/api/auth/signin`  | User login | Public |
| `POST` | `/api/auth/signout` | User logout | Protected |

### Product Endpoints

| Method   | Endpoint                                        | Description                  | Access |
|----------|-------------------------------------------------|------------------------------|--------|
| `GET`    | `/api/public/product`                           | Get all products (paginated) | Public |
| `GET`    | `/api/public/category/{categoryId}/product`     | Get product by category      | Public |
| `GET`    | `/api/public/product/keyword/{keyword}`         | Get product by keyword       | Public |
| `POST`   | `api/admin/category/{categoryId}/product`       | Create new product           | Admin  |
| `PUT`    | `/api/admin/category/{categoryId}/product/{id}` | Update product               | Admin  |
| `PUT`    | ` api/admin/product/{id}/image`                 | Update product image         | Admin  |
| `DELETE` | `/api/admin/products/{id}`                      | Delete product               | Admin  |

### Cart Endpoints

| Method | Endpoint                                                 | Description      | Access    |
|--------|----------------------------------------------------------|------------------|-----------|
| `GET` | `/api/cart`                                              | Get all cart     | Protected |
|  `GET`      | `/api/user/cart`                                         | Get user's cart  | Protected |
| `POST` | `api/cart/products/{productId}/quantity/{quantity}`      | Add item to cart | Protected |
| `PUT` | `api/user/cart/product/{prductId}/quantity/{add/delete}` | Update cart item | Protected |
| `DELETE` | `/api/user/cart/{cartId}/product/{productId}`            | Remove cart item | Protected |

### Place Order Endpoints

| Method | Endpoint | Description       | Access |
|--------|----------|-------------------|--------|
| `POST` | `/api/order/user/payments/UPImethod` | Placing the order | Protected |


### Sample Request/Response

**POST** `/api/auth/login`
```json
// Request
{
  "email": "user@example.com",
  "password": "password123"
}

// Response
//Multiple roles here
{
   "id": 1,
   "jwtToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMSIsImlhdCI6MTc2NzU1MTE0MiwiZXhwIjoxNzY3NTU0MTQyfQ.PNrefJHIvRUjjCIx5ovt5L2nxzcKnTN0hDN6NMq3hAY",
   "username": "user",
   "roles": [
      "ROLES_USER"
      
   ]
}
```

---

## 🔒 Security Features

### JWT Token Authentication
- Stateless authentication mechanism
- Token expiration and refresh strategy
- Secure cookie storage for tokens
- Role-based access control

### Password Security
- BCrypt password hashing
- Strong password validation
- Secure password reset flow

### API Security
- CORS configuration
- CSRF protection
- XSS prevention
- SQL injection protection via JPA

---

## 🎯 Key Highlights

### Exception Handling
Centralized global exception handler for consistent API responses:
- `ResourceNotFoundException` - 404 Not Found
- `APIException` - 400 Bad Request
- `UnauthorizedException` - 401 Unauthorized
- `ValidationException` - 422 Unprocessable Entity

### Data Validation
Comprehensive validation using Jakarta Bean Validation:
```java
@NotBlank(message = "Email is required")
@Email(message = "Email should be valid")
private String email;

@Size(min = 8, message = "Password must be at least 8 characters")
private String password;
```

### Pagination Support
```java
GET /api/public/categoru?page=0&size=10&sortBy=price&sortDir=asc
```

---

## 🔮 Future Enhancements

- [ ] 💳 Payment gateway integration (Stripe/PayPal)
- [ ] 📦 Order status tracking & notifications
- [ ] ⭐ Product reviews and ratings system
- [ ] 📊 Admin analytics dashboard
- [ ] 🔍 Advanced search and filtering
- [ ] 📧 Email notification service
- [ ] 🐳 Docker containerization
- [ ] ☁️ AWS/Cloud deployment
- [ ] 📝 Swagger/OpenAPI documentation
- [ ] 🧪 Comprehensive test coverage

---

## 🧪 Testing

### Run Tests
```bash
mvn test
```

### Postman Collection
Import the provided Postman collection for easy API testing:
1. Open Postman
2. Import `ecommerce-api.postman_collection.json`
3. Set environment variables
4. Start testing endpoints

---

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request



---

## 👨‍💻 Author

**Ayush Sharma**

Backend Developer | Spring Boot Specialist | REST API Architect

[![GitHub](https://img.shields.io/badge/GitHub-100000?style=for-the-badge&logo=github&logoColor=white)](https://github.com/KennyAck001)
[![LinkedIn](https://img.shields.io/badge/LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=white)](https://linkedin.com/in/ayush-sharma-2a34ba345)
---

## 🌟 Show Your Support

Give a ⭐️ if this project helped you or you found it useful!

---

<div align="center">

**Built with ❤️ using Spring Boot**

</div>