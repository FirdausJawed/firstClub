# 🚀 How to Run FirstClub Application

## ✅ Prerequisites Check

Your system is ready with:
- ✅ **Java 22** (OpenJDK Corretto 22.0.2) - Compatible with Java 17 requirement
- ✅ **Maven Wrapper** - Included in project (no installation needed)
- ✅ **H2 Database** - Configured and ready to use

---

## 🎯 Quick Start (3 Steps)

### Step 1: Open Terminal
Navigate to your project directory:
```bash
cd /Users/panand5/IdeaProjects/firstclub
```

### Step 2: Run the Application
```bash
./mvnw spring-boot:run
```

### Step 3: Verify It's Running
You should see output ending with:
```
Started FirstclubApplication in X.XXX seconds (JVM running for Y.YYY)
```

**🎉 Success!** Your application is now running on **http://localhost:8080**

---

## 🌐 Access Points

### 1. Application Base URL
```
http://localhost:8080
```
Currently returns 404 (no controllers defined yet - this is normal)

### 2. H2 Database Console
```
http://localhost:8080/h2-console
```

**Login Credentials:**
- **JDBC URL**: `jdbc:h2:mem:firstclubdb`
- **Username**: `sa`
- **Password**: (leave empty)
- Click **"Connect"**

---

## 🛑 Stop the Application

Press `Ctrl + C` in the terminal where the application is running.

---

## 🔄 Alternative Ways to Run

### Method 1: Build JAR and Run

#### Build:
```bash
./mvnw clean package
```

#### Run:
```bash
java -jar target/firstclub-0.0.1-SNAPSHOT.jar
```

### Method 2: Using IDE

#### IntelliJ IDEA:
1. Open the project
2. Navigate to `src/main/java/com/org/firstclub/FirstclubApplication.java`
3. Right-click on the file
4. Select **"Run 'FirstclubApplication'"**

#### Eclipse:
1. Import as Maven project
2. Right-click on `FirstclubApplication.java`
3. Select **Run As → Java Application**

#### VS Code:
1. Open the project
2. Install "Spring Boot Extension Pack" if not already installed
3. Press `F5` or use the Run menu
4. Select **"Spring Boot App"**

---

## 📋 Configuration Summary

### Application Settings
All configurations are in: `src/main/resources/application.properties`

| Setting | Value |
|---------|-------|
| **Application Name** | firstclub |
| **Server Port** | 8080 |
| **Database Type** | H2 In-Memory |
| **Database URL** | jdbc:h2:mem:firstclubdb |
| **H2 Console** | Enabled at /h2-console |
| **JPA DDL** | Auto-update |
| **SQL Logging** | Enabled |

---

## 🧪 Run Tests

### Run All Tests:
```bash
./mvnw test
```

### Run Specific Test:
```bash
./mvnw test -Dtest=FirstclubApplicationTests
```

### Run Tests with Coverage:
```bash
./mvnw clean test
```

---

## 🔧 Common Build Commands

### Clean Build:
```bash
./mvnw clean install
```

### Build Without Tests:
```bash
./mvnw clean package -DskipTests
```

### Check Dependencies:
```bash
./mvnw dependency:tree
```

### Verify Project:
```bash
./mvnw verify
```

---

## ⚠️ Troubleshooting

### Problem: Port 8080 is already in use

**Error Message:**
```
Web server failed to start. Port 8080 was already in use.
```

**Solution 1** - Change Port:
Edit `src/main/resources/application.properties`:
```properties
server.port=8081
```

**Solution 2** - Kill Process on Port 8080:
```bash
# Find process
lsof -i :8080

# Kill process (replace PID with actual process ID)
kill -9 PID
```

### Problem: Maven wrapper not executable

**Error Message:**
```
Permission denied: ./mvnw
```

**Solution:**
```bash
chmod +x mvnw
```

### Problem: Java version issues

**Error Message:**
```
Unsupported class file major version XX
```

**Solution:**
Check Java version:
```bash
java -version
```

Ensure Java 17 or higher is installed and set as JAVA_HOME.

### Problem: Build fails

**Solution:**
Try a clean build:
```bash
./mvnw clean install -U
```

The `-U` flag forces update of dependencies.

### Problem: Lombok not working

**Solution for IntelliJ IDEA:**
1. Install Lombok plugin: Settings → Plugins → Search "Lombok" → Install
2. Enable annotation processing: Settings → Build, Execution, Deployment → Compiler → Annotation Processors → Enable annotation processing

**Solution for Eclipse:**
1. Download lombok.jar from https://projectlombok.org/download
2. Run: `java -jar lombok.jar`
3. Select Eclipse installation directory
4. Click "Install/Update"

**Solution for VS Code:**
1. Install "Lombok Annotations Support for VS Code" extension

---

## 📊 Expected Console Output

When you run the application, you should see output similar to:

```
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/

 :: Spring Boot ::                (v4.0.0)

2025-11-24 ... INFO ... Starting FirstclubApplication using Java 22.0.2
2025-11-24 ... INFO ... No active profile set, falling back to 1 default profile: "default"
2025-11-24 ... INFO ... Bootstrapping Spring Data JPA repositories
2025-11-24 ... INFO ... Finished Spring Data repository scanning
2025-11-24 ... INFO ... Tomcat initialized with port 8080 (http)
2025-11-24 ... INFO ... Starting service [Tomcat]
2025-11-24 ... INFO ... Starting Servlet engine: [Apache Tomcat/10.1.x]
2025-11-24 ... INFO ... Initializing Spring embedded WebApplicationContext
2025-11-24 ... INFO ... HHH000204: Processing PersistenceUnitInfo [name: default]
2025-11-24 ... INFO ... HHH000412: Hibernate ORM core version [6.x.x]
2025-11-24 ... INFO ... HHH000400: Using dialect: org.hibernate.dialect.H2Dialect
2025-11-24 ... INFO ... HHH000490: Using JtaPlatform implementation: [org.hibernate.engine.transaction.jta.platform.internal.NoJtaPlatform]
2025-11-24 ... INFO ... Initialized JPA EntityManagerFactory for persistence unit 'default'
2025-11-24 ... INFO ... H2 console available at '/h2-console'. Database available at 'jdbc:h2:mem:firstclubdb'
2025-11-24 ... INFO ... Tomcat started on port 8080 (http) with context path '/'
2025-11-24 ... INFO ... Started FirstclubApplication in X.XXX seconds (process running for Y.YYY)
```

---

## 🎯 Next Steps After Running

1. **Verify H2 Console Access**
   - Go to http://localhost:8080/h2-console
   - Login with provided credentials
   - Explore the database (currently empty)

2. **Create Your First Controller**
   - Add a REST controller to handle HTTP requests
   - Test with browser or Postman

3. **Add Entities**
   - Create JPA entities for your data model
   - Tables will be auto-created in H2 database

4. **Write Tests**
   - Add unit tests for your services
   - Add integration tests for your controllers

---

## 📚 Documentation Files

- **README.md** - Complete project documentation
- **QUICKSTART.md** - Quick start guide
- **APPLICATION_DETAILS.md** - Technical details and configuration
- **RUN_INSTRUCTIONS.md** - This file

---

## 🆘 Need Help?

If you encounter issues not covered here:

1. Check the full **README.md** for detailed troubleshooting
2. Review **APPLICATION_DETAILS.md** for configuration details
3. Check Spring Boot logs for specific error messages
4. Verify all prerequisites are correctly installed

---

**Happy Coding! 🎉**

---

**Last Updated**: 2025-11-24  
**Application Version**: 0.0.1-SNAPSHOT  
**Spring Boot Version**: 4.0.0

