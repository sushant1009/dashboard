# Finance Dashboard API

![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/SpringBoot-3.x-brightgreen)
![Security](https://img.shields.io/badge/Security-JWT-blue)
![Status](https://img.shields.io/badge/Status-Production--Ready-success)
![License](https://img.shields.io/badge/License-MIT-lightgrey)

---

## Overview

A **secure and scalable backend system** for managing financial transactions and generating insights.

Built using **Spring Boot**, this project demonstrates:
- Clean architecture
- Role-based access control
- JWT authentication
- Rate limiting for API protection
- Real-world dashboard analytics

---

## Features

* JWT-based Authentication & Authorization
* Role-Based Access (ADMIN, ANALYST, VIEWER)
* Transaction Management (Create, Update, Delete, Read, Filter)
* Dashboard Insights (Summary & Category Analysis)
* Rate Limiting (10 requests/min per user)
*  Global Exception Handling
*  Swagger API Documentation

---

## Architecture

The application follows a **layered architecture**:

Controller → Service → Repository → Database

* **Controller Layer** → Handles HTTP requests and responses
* **Service Layer** → Contains business logic
* **Repository Layer** → Interacts with database
* **Security Layer** → JWT authentication & authorization

---

## Tech Stack

| Layer      | Technology            |
| ---------- |-----------------------|
| Backend    | Spring Boot (Java 17) |
| Security   | Spring Security + JWT |
| Database   | MySQL                 |
| Build Tool | Maven                 |
| API Docs   | Swagger (OpenAPI)     |

---

##  Authentication

All secured APIs require a JWT token.

### Header Format:

Authorization: Bearer <JWT_TOKEN>

---

## Roles & Permissions

| Role    | Access Level        |
| ------- | ------------------- |
| ADMIN   | Full system access  |
| ANALYST | Read + analytics    |
| VIEWER  | Limited to own data |

---

##  Rate Limiting

* Limit: **10 requests per minute per user**
* Implemented using a filter with in-memory tracking

### Response:

HTTP 429 - Too Many Requests

---

##  API Endpoints

###  Authentication APIs

| Method | Endpoint       | Description              |
| ------ | -------------- | ------------------------ |
| POST   | /auth/signup | Register a new user      |
| POST   | /auth/login    | Authenticate and get JWT |

#### Request Body - Signup
```
{
  "name": "Analyst User",
  "email": "analyst@gmail.com",
  "password":1234 ,
  "role": "ANALYST",
  "status":"ACTIVE"
}
```

#### Request Body - Login
```
{
  "email": "analyst@gmail.com",
  "password": "1234"
}
```

---

###  User APIs (ADMIN Only)

| Method | Endpoint       | Description                                         |
|--------|----------------|-----------------------------------------------------|
| GET    | /users         | Get all users                                       |
| GET    | /users/{id}    | Get user by ID                                      |
| PUT    | /change-role   | Change the role(ACTIVE, INACTIVE) of a user         |
| PUT    | /change-status | Change the status(ADMIN, ANALYST, VIEWER) of a user |

##### Request Body(role)
```
{
  "userId": 1,
  "role": "ADMIN"
}
```

---

##### Request Body(status)
```
{
  "userId": 1,
  "status": "Active"
}
```

---

###  Transaction APIs

| Method | Endpoint                      | Description          |
|--------|-------------------------------|----------------------|
| POST   | /transactions/admin/create    | Create a transaction |
| GET    | /transactions/                | Get all transactions |
| GET    | /transactions?type=<**TYPE**> | Filter transactions  |
| PUT    | /transactions/admin/{id}      | Update a transaction |
| DELETE | /transactions/admin/{id}      | Delete a transaction |

#### Create transaction Request Body 

```
{
  "amount": 40000,
  "type": "INCOME",
  "category": "Salary",
  "date": "2026-04-01",
  "note": "User2 salary",
  "userId": 2
}
```


#### Query Parameters(Filter):

* type → INCOME / EXPENSE
* category → String
* start → LocalDate (yyyy-MM-dd)
* end → LocalDate (yyyy-MM-dd)

---

###  Dashboard APIs

#### Summary

GET /dashboard/summary

With filters:
```
GET /dashboard/summary?start=2026-07-01&end=2026-07-30
```

---

#### Category Summary

GET /dashboard/category

With filters:
```
GET /dashboard/category?type=EXPENSE&start=2026-07-01&end=2026-07-30
```
---

#### Category Summary (Admin - Specific User)

```
GET /dashboard/category/{userId}
```

---

##  Validation & Error Handling

| Status Code | Description                             |
|-------------|-----------------------------------------|
| 400         | Invalid input / validation error        |
| 401         | Unauthorized / Session expired          |
| 403         | Forbidden (unauthorized role)           |
| 429         | Too many requests (rate limit exceeded) |
| 500         | Internal server error                   |

### Example Errors

Invalid date range:
{
"error": "Start date cannot be after end date"
}

- Invalid enum: type=***Expense***
- valid enum: type=***EXPENSE*** 

---

## 📑 Swagger Documentation

Access API documentation via:
```
http://localhost:8080/swagger-ui/index.html
```

---

##  Setup Instructions

### 1 Clone Repository

```
git clone https://github.com/sushant1009/finance-dashboard.git
cd finance-dashboard
```

---

### 2 Configure Database

Update `application.properties`:
```

spring.datasource.url=jdbc:mysql://localhost:3306/finance_db
spring.datasource.username=root
spring.datasource.password=your_password

````

---

### 3 Run Application

```
mvn spring-boot:run
```

---

##  Key Concepts Demonstrated

* Layered backend architecture
* Secure API development with JWT
* Role-based authorization
* Rate limiting for API protection
* Data filtering and aggregation logic

---



##  Author

**Sushant Sabale**


---





