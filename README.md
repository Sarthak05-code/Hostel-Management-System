# Course Recommender

A web application built with **Spring Boot**, **Thymeleaf**, and **MySQL** that recommends courses based on user input.

> 🚀 My first Spring Boot project, developed after approximately **6 months of learning Java**.

---

## ✨ Features

- Course recommendations based on user input
- Responsive and clean UI built with Tailwind CSS
- MySQL database integration
- Spring Data JPA for database operations
- DTO pattern for cleaner data transfer
- Layered architecture (Controller → Service → Repository)

---

## 🛠️ Tech Stack

| Category  | Technology                         |
| --------- | ---------------------------------- |
| Backend   | Spring Boot, Java                  |
| Frontend  | Thymeleaf, HTML, CSS, Tailwind CSS |
| Database  | MySQL                              |
| ORM       | Spring Data JPA                    |
| Utilities | Lombok, DTOs                       |

---

## 📋 Prerequisites

Before running this project, make sure you have:

- Java 25 or higher
- Maven
- MySQL Server
- IntelliJ IDEA or VS Code or any IDEs you use

---

## ⚙️ Database Configuration

### Step 1: Create Database

```sql
CREATE DATABASE Main;
```

### Step 2: Configure Application Properties

Open:

```properties
src/main/resources/application.properties
```

Update the following values:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/Main
spring.datasource.username=root
spring.datasource.password=yourpassword
```

---

## ▶️ Running the Application

Clone the repository and start the application:

```bash
git clone <your-repository-url>

cd course-recommender

mvn spring-boot:run
```

Application will be available at:

```text
http://localhost:8080
```

---

## 📁 Project Structure

```text
course-recommender/
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── controller/
│   │   │   ├── service/
│   │   │   ├── repository/
│   │   │   ├── dto/
│   │   │   └── model/
│   │   │
│   │   └── resources/
│   │       ├── templates/
│   │       ├── static/
│   │       └── application.properties
│   │
│   └── test/
│
├── pom.xml
└── README.md
```

---

## 🎯 Learning Objectives

This project helped me learn:

- Spring Boot fundamentals
- Dependency Injection
- MVC Architecture
- Spring Data JPA
- DTO Design Pattern
- Thymeleaf Templating
- MySQL Integration

---

## 🚧 Future Improvements

- Improved exception handling and validation
- User authentication and authorization
- Course rating and review system
- Advanced search and filtering
- REST API support
- Unit and integration testing
- Modern frontend using React instead of Thymeleaf
- Docker containerization
- Performance optimizations
- Additional recommendation features and functionality

---

## 📝 Notes

This is my **first Spring Boot project** and serves as a milestone in my Java learning journey.

While there is still room for improvement, it reflects my understanding of:

- Java Fundamentals
- Spring Boot
- Database Integration
- MVC Architecture
- Frontend Templating with Thymeleaf

Feedback and suggestions are always welcome!
