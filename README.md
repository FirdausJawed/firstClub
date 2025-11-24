# FirstClub Application

A Spring Boot application built with Java 17, Spring Data JPA, and H2 database.

## Table of Contents
- [Prerequisites](#prerequisites)
- [Technology Stack](#technology-stack)
- [Project Structure](#project-structure)
- [Configuration](#configuration)
- [How to Run](#how-to-run)
- [Accessing the Application](#accessing-the-application)
- [Development](#development)
- [Testing](#testing)
- [Troubleshooting](#troubleshooting)

## Prerequisites

Before running this application, ensure you have the following installed:

- **Java 17** or higher
  - Check version: `java -version`
  - Download from: [Oracle JDK](https://www.oracle.com/java/technologies/downloads/) or [OpenJDK](https://adoptium.net/)

- **Maven** (Optional - Maven wrapper is included)
  - The project includes Maven wrapper (`mvnw` for Unix/Mac, `mvnw.cmd` for Windows)
  - If you prefer to use your own Maven installation, ensure Maven 3.6+ is installed

## Technology Stack

- **Spring Boot**: 4.0.0
- **Java**: 17
- **Database**: H2 (in-memory)
- **ORM**: Spring Data JPA / Hibernate
- **Build Tool**: Maven 3.9.11
- **Additional Libraries**:
  - Lombok (for reducing boilerplate code)
  - Spring Validation
  - Spring Web MVC

## Project Structure

```
firstclub/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/org/firstclub/
│   │   │       └── FirstclubApplication.java
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── static/
│   │       └── templates/
│   └── test/
│       └── java/
│           └── com/org/firstclub/
│               └── FirstclubApplicationTests.java
├── pom.xml
├── mvnw (Maven wrapper for Unix/Mac)
├── mvnw.cmd (Maven wrapper for Windows)
└── README.md
```

## Configuration

The application is configured via `src/main/resources/application.properties`:

### Key Configurations:

- **Server Port**: 8080
- **Database**: H2 in-memory database
  - URL: `jdbc:h2:mem:firstclubdb`
  - Username: `sa`
  - Password: (empty)
- **H2 Console**: Enabled at `/h2-console`
- **JPA**: Auto-update schema, SQL logging enabled

## How to Run

### Option 1: Using Maven Wrapper (Recommended)

#### On Unix/Mac/Linux:
```bash
# Make the wrapper executable (first time only)
chmod +x mvnw

# Run the application
./mvnw spring-boot:run
```

#### On Windows:
```cmd
mvnw.cmd spring-boot:run
```

### Option 2: Using Installed Maven

```bash
mvn spring-boot:run
```

### Option 3: Build and Run JAR

#### Build the JAR:
```bash
# Unix/Mac/Linux
./mvnw clean package

# Windows
mvnw.cmd clean package
```

#### Run the JAR:
```bash
java -jar target/firstclub-0.0.1-SNAPSHOT.jar
```

### Option 4: Run from IDE

1. Import the project as a Maven project in your IDE (IntelliJ IDEA, Eclipse, VS Code)
2. Locate the main class: `com.org.firstclub.FirstclubApplication`
3. Right-click and select "Run" or "Debug"

## Accessing the Application

Once the application starts successfully, you'll see output similar to:
```
Started FirstclubApplication in X.XXX seconds
```

### Application Endpoints:

- **Application Base URL**: http://localhost:8080
- **H2 Database Console**: http://localhost:8080/h2-console
  - JDBC URL: `jdbc:h2:mem:firstclubdb`
  - Username: `sa`
  - Password: (leave empty)

### Health Check:

Currently, the application is a skeleton project. You can verify it's running by:
- Checking the console logs for "Started FirstclubApplication"
- Accessing the H2 console at http://localhost:8080/h2-console

## Development

### Adding New Features:

The project structure follows standard Spring Boot conventions:

1. **Controllers**: Create in `src/main/java/com/org/firstclub/controller/`
2. **Services**: Create in `src/main/java/com/org/firstclub/service/`
3. **Repositories**: Create in `src/main/java/com/org/firstclub/repository/`
4. **Entities**: Create in `src/main/java/com/org/firstclub/entity/` or `model/`
5. **DTOs**: Create in `src/main/java/com/org/firstclub/dto/`

### Using Lombok:

The project includes Lombok. Common annotations:
- `@Data` - Generates getters, setters, toString, equals, hashCode
- `@NoArgsConstructor` - Generates no-args constructor
- `@AllArgsConstructor` - Generates all-args constructor
- `@Builder` - Implements builder pattern

### Hot Reload (Optional):

Add Spring Boot DevTools for automatic restart during development:
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-devtools</artifactId>
    <scope>runtime</scope>
    <optional>true</optional>
</dependency>
```

## Testing

### Run All Tests:

```bash
# Unix/Mac/Linux
./mvnw test

# Windows
mvnw.cmd test
```

### Run Specific Test:

```bash
./mvnw test -Dtest=FirstclubApplicationTests
```

### Test Coverage:

```bash
./mvnw clean test jacoco:report
```

## Troubleshooting

### Issue: Port 8080 already in use

**Solution**: Change the port in `application.properties`:
```properties
server.port=8081
```

### Issue: Java version mismatch

**Error**: `Unsupported class file major version XX`

**Solution**: Ensure Java 17 is installed and set as JAVA_HOME:
```bash
# Check Java version
java -version

# Set JAVA_HOME (Unix/Mac)
export JAVA_HOME=/path/to/java17

# Set JAVA_HOME (Windows)
set JAVA_HOME=C:\path\to\java17
```

### Issue: Maven wrapper not executable

**Solution** (Unix/Mac):
```bash
chmod +x mvnw
```

### Issue: Build fails with Lombok errors

**Solution**: Ensure your IDE has Lombok plugin installed:
- **IntelliJ IDEA**: Install "Lombok" plugin and enable annotation processing
- **Eclipse**: Install Lombok from https://projectlombok.org/
- **VS Code**: Install "Lombok Annotations Support" extension

### Issue: Cannot access H2 console

**Solution**: Verify these settings in `application.properties`:
```properties
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
```

### Clean Build:

If you encounter build issues, try a clean build:
```bash
./mvnw clean install
```

## Additional Commands

### Skip Tests During Build:
```bash
./mvnw clean package -DskipTests
```

### Run in Different Profiles:
```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

### Check Dependencies:
```bash
./mvnw dependency:tree
```

### Update Dependencies:
```bash
./mvnw versions:display-dependency-updates
```

## Next Steps

This is a skeleton Spring Boot application. To build a complete application, consider adding:

1. REST Controllers for your API endpoints
2. JPA Entities for your data models
3. Service layer for business logic
4. Repository interfaces for data access
5. Exception handling
6. Security configuration (Spring Security)
7. API documentation (Swagger/OpenAPI)
8. Additional tests (unit and integration tests)

## Support

For Spring Boot documentation, visit:
- [Spring Boot Reference Documentation](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- [Spring Data JPA Documentation](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/)

---

**Version**: 0.0.1-SNAPSHOT  
**Last Updated**: 2025-11-24

