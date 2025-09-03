# Spring Boot Notes API

A RESTful API for managing notes, built with Spring Boot and following Astrapay's conventions.

## 🚀 Prerequisites

- Java 11 or higher
- Maven 3.6.3 or higher
- Your favorite IDE (IntelliJ IDEA, Eclipse, VS Code, etc.)

## 📦 Dependencies

The project uses the following key dependencies:

- **Spring Boot 2.7.18**
  - spring-boot-starter-web
  - spring-boot-starter-validation
  - spring-boot-starter-data-jpa
- **Lombok** - For reducing boilerplate code
- **SpringFox Swagger** - For API documentation
- **H2 Database** - In-memory database (for development)

## 🛠️ Installation

1. Clone the repository:
   ```bash
   git clone [repository-url]
   cd astrapay-spring-boot-external
   ```

2. Build the project:
   ```bash
   mvn clean install
   ```

## 🚀 Running the Application

### Running with Maven
```bash
mvn spring-boot:run
```

### Running the JAR file
```bash
mvn package
java -jar target/astrapay-spring-boot-external-1.0-SNAPSHOT.jar
```

## 📝 API Endpoints

- `GET /notes` - Get all notes
- `POST /notes` - Create a new note
- `PUT /notes/{id}` - Update a note
- `DELETE /notes/{id}` - Delete a note

## 🧪 Testing

Run the tests using:
```bash
mvn test
```

## 📦 Project Structure

```
com.astrapay
├── config           # Configuration classes
├── controller       # REST controllers
├── dto              # Data Transfer Objects
├── entity           # JPA entities
├── exception        # Custom exceptions and handlers
├── repository       # Data access layer
└── service          # Business logic layer
```
