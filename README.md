# Course Recommender

A web application built with Spring Boot, Thymeleaf, and MySQL that recommends courses based on user input. This is my first Spring Boot project, developed after 6 months of learning Java.

---

## Tech Stack

| Layer    | Technology                         |
| -------- | ---------------------------------- |
| Backend  | Spring Boot, Java                  |
| Frontend | Thymeleaf, HTML, CSS, Tailwind CSS |
| Database | MySQL                              |
| ORM      | Spring Data JPA                    |
| Other    | Lombok, DTOs                       |

---

## Features

- Course recommendation based on user input
- Clean and responsive UI with Tailwind CSS
- MySQL database integration via Spring Data JPA
- DTO pattern for clean data transfer between layers

---

## Setup Instructions

### 1. Requirements

- Java 25+
- Maven
- MySQL
- IDE (IntelliJ / VS Code)

### 2. Database Setup

1. Create a MySQL database named `Main`
2. Update credentials in `application.properties`:

- spring.datasource.url=jdbc:mysql://localhost:3306/Main
- spring.datasource.username=root
- spring.datasource.password=yourpassword

### 3. Run the App

```bash
cd course-recommender
mvn spring-boot:run
```

Then open: `http://localhost:8080`

---

## Project Structure

course-recommender/

├── src/

│ ├── main/

│ │ ├── java/ # Controllers, Services, Repositories, DTOs

│ │ └── resources/

│ │ ├── templates/ # Thymeleaf HTML templates

│ │ └── application.properties

│ └── test/

└── pom.xml

---

## Notes

- First Spring Boot project — error handling is still a work in progress
- Built after 6 months of learning Java as a foundation
