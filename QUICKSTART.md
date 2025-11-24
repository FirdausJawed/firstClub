# Quick Start Guide - FirstClub Application

## ⚡ Fastest Way to Run

### Step 1: Navigate to Project Directory
```bash
cd /Users/panand5/IdeaProjects/firstclub
```

### Step 2: Run the Application
```bash
./mvnw spring-boot:run
```

That's it! The application will start on **http://localhost:8080**

---

## 🎯 What You Can Do Now

### 1. Verify Application is Running
Look for this message in the console:
```
Started FirstclubApplication in X.XXX seconds
```

### 2. Access H2 Database Console
- URL: **http://localhost:8080/h2-console**
- JDBC URL: `jdbc:h2:mem:firstclubdb`
- Username: `sa`
- Password: (leave empty)
- Click "Connect"

### 3. Stop the Application
Press `Ctrl + C` in the terminal

---

## 📋 System Information

✅ **Java Version Detected**: OpenJDK 22.0.2 (Compatible with Java 17 requirement)  
✅ **Maven Wrapper**: Included (no need to install Maven)  
✅ **Database**: H2 in-memory (auto-configured)  
✅ **Server Port**: 8080

---

## 🔧 Common Commands

### Build the Project
```bash
./mvnw clean install
```

### Run Tests
```bash
./mvnw test
```

### Build JAR File
```bash
./mvnw clean package
```

### Run the JAR
```bash
java -jar target/firstclub-0.0.1-SNAPSHOT.jar
```

### Skip Tests During Build
```bash
./mvnw clean package -DskipTests
```

---

## 🚀 Next Steps

1. **Add a REST Controller**: Create your first API endpoint
2. **Create Entities**: Define your data models
3. **Add Business Logic**: Implement services
4. **Write Tests**: Add unit and integration tests

---

## 📚 Need More Details?

See the full **README.md** for:
- Complete configuration details
- Troubleshooting guide
- Development best practices
- Technology stack information

---

## ⚠️ Troubleshooting

### Port 8080 Already in Use?
Change the port in `src/main/resources/application.properties`:
```properties
server.port=8081
```

### Build Errors?
Try a clean build:
```bash
./mvnw clean install
```

### Maven Wrapper Not Executable?
```bash
chmod +x mvnw
```

---

**Ready to code!** 🎉

