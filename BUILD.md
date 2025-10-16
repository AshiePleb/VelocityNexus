# 🔨 Building Velocity Nexus

This guide shows you how to build both JAR files from source.

---

## 📋 Prerequisites

Before building, ensure you have:

- **Java Development Kit (JDK) 17** or higher
- **Apache Maven 3.6+**
- **Git** (to clone the repository)
- **Internet connection** (Maven downloads dependencies)

### Check Your Java Version

```powershell
# Windows PowerShell
java -version
```

You should see output like:
```
java version "17.0.x" or higher
```

If Java 17+ is not installed, download it from:
- [Adoptium](https://adoptium.net/) (recommended)
- [Oracle JDK](https://www.oracle.com/java/technologies/downloads/)
- [Amazon Corretto](https://aws.amazon.com/corretto/)

### Check Maven Installation

```powershell
# Windows PowerShell
mvn -version
```

If Maven is not installed, download it from [Apache Maven](https://maven.apache.org/download.cgi).

---

## 🚀 Building the Plugins

### Step 1: Navigate to Project Directory

```powershell
# Windows PowerShell
cd "C:\Users\AshiePleb\Documents\Development\Minecraft Plugins\Velocity Nexus"
```

### Step 2: Build the Proxy Plugin

```powershell
# Clean previous builds and compile
mvn clean package
```

**Expected Output:**
```
[INFO] BUILD SUCCESS
[INFO] Total time: 2-5 seconds
[INFO] Finished at: 2025-10-15T...
```

**Output Location:**
```
target/velocitynexus-1.0.0-velocity.jar
```

### Step 3: Build the Backend Plugin

```powershell
# Navigate to backend directory
cd backend

# Clean and compile backend plugin
mvn clean package

# Return to main directory
cd ..
```

**Expected Output:**
```
[INFO] BUILD SUCCESS
[INFO] Total time: 2-5 seconds
[INFO] Finished at: 2025-10-15T...
```

**Output Location:**
```
backend/target/velocitynexus-1.0.0-spigot.jar
```

---

## 📦 Build Artifacts

After successful builds, you'll have:

| File | Location | Install To |
|------|----------|------------|
| **Proxy Plugin** | `target/velocitynexus-1.0.0-velocity.jar` | Velocity `/plugins/` folder |
| **Backend Plugin** | `backend/target/velocitynexus-1.0.0-spigot.jar` | Each Spigot/Paper server's `/plugins/` folder |

---

## 🔧 Build Options

### Quick Build (Skip Tests)

```powershell
# Proxy plugin
mvn clean package -DskipTests

# Backend plugin
cd backend ; mvn clean package -DskipTests ; cd ..
```

### Clean Build (Remove All Old Files)

```powershell
# Clean everything
mvn clean
cd backend ; mvn clean ; cd ..
```

### Build Both Plugins (One Command)

```powershell
# Build proxy, then backend
mvn clean package ; cd backend ; mvn clean package ; cd ..
```

---

## 🐛 Troubleshooting

### Build Fails with "JAVA_HOME not set"

**Windows:**
```powershell
# Set JAVA_HOME temporarily
$env:JAVA_HOME = "C:\Program Files\Java\jdk-17"
$env:PATH = "$env:JAVA_HOME\bin;$env:PATH"

# Verify
java -version
```

To set permanently:
1. Open **System Properties** → **Environment Variables**
2. Add `JAVA_HOME` = `C:\Program Files\Java\jdk-17`
3. Add `%JAVA_HOME%\bin` to `PATH`

### Build Fails with "mvn not found"

**Windows:**
1. Download Maven from [Apache Maven](https://maven.apache.org/download.cgi)
2. Extract to `C:\Program Files\Apache\maven`
3. Add `C:\Program Files\Apache\maven\bin` to PATH
4. Restart PowerShell

### Build Fails with Compilation Errors

1. **Check Java version**: Must be Java 17+
   ```powershell
   java -version
   ```

2. **Clean and rebuild**:
   ```powershell
   mvn clean package
   ```

3. **Update dependencies**:
   ```powershell
   mvn clean install -U
   ```

### Backend Plugin Build Fails

1. **Navigate to backend directory first**:
   ```powershell
   cd backend
   mvn clean package
   ```

2. **Check backend pom.xml exists**:
   ```powershell
   Test-Path backend/pom.xml  # Should return True
   ```

### "BUILD SUCCESS" but no JAR file?

1. **Check target directory**:
   ```powershell
   # Proxy
   ls target/*.jar
   
   # Backend
   ls backend/target/*.jar
   ```

2. **Rebuild with verbose output**:
   ```powershell
   mvn clean package -X
   ```

---

## ✅ Verifying Build Success

### Check File Sizes

```powershell
# Proxy plugin (should be ~50-100 KB)
ls target/velocitynexus-1.0.0-velocity.jar

# Backend plugin (should be ~30-60 KB)
ls backend/target/velocitynexus-1.0.0-spigot.jar
```

### Check JAR Contents

```powershell
# List contents of proxy JAR
jar tf target/velocitynexus-1.0.0-velocity.jar

# Should contain:
# - velocity-plugin.json
# - com/ashiepleb/velocityNexus/*.class files
```

```powershell
# List contents of backend JAR
jar tf backend/target/velocitynexus-1.0.0-spigot.jar

# Should contain:
# - plugin.yml
# - com/ashiepleb/velocityNexus/backend/*.class files
```

---

## 📂 Project Structure

```
Velocity Nexus/
├── pom.xml                           ← Proxy plugin configuration
├── src/                              ← Proxy plugin source
│   └── main/
│       ├── java/
│       │   └── com/ashiepleb/velocityNexus/
│       │       ├── VelocityNexus.java
│       │       ├── command/
│       │       ├── config/
│       │       └── menu/
│       └── resources/
│           └── velocity-plugin.json
├── target/
│   └── velocitynexus-1.0.0-velocity.jar      ← Built proxy plugin
│
└── backend/
    ├── pom.xml                       ← Backend plugin configuration
    ├── src/                          ← Backend plugin source
    │   └── main/
    │       ├── java/
    │       │   └── com/ashiepleb/velocityNexus/backend/
    │       │       ├── VelocityNexusBackend.java
    │       │       ├── listener/
    │       │       └── menu/
    │       └── resources/
    │           └── plugin.yml
    └── target/
        └── velocitynexus-1.0.0-spigot.jar  ← Built backend plugin
```

---

## 🎯 Next Steps

After building successfully:

1. **Copy plugins to servers**:
   ```powershell
   # Proxy plugin
   Copy-Item "target\velocitynexus-1.0.0-velocity.jar" "C:\Path\To\Velocity\plugins\"
   
   # Backend plugin (repeat for each server)
   Copy-Item "backend\target\velocitynexus-1.0.0-spigot.jar" "C:\Path\To\Server\plugins\"
   ```

2. **Restart servers**:
   - Restart Velocity proxy
   - Restart all backend servers

3. **Test the plugin**:
   - Join your network
   - Type `/server`
   - GUI should open!

---

## 📚 Maven Commands Reference

| Command | Description |
|---------|-------------|
| `mvn clean` | Delete all built files |
| `mvn compile` | Compile source code only |
| `mvn package` | Compile and create JAR |
| `mvn clean package` | Clean, then build JAR |
| `mvn clean install` | Build and install to local Maven repo |
| `mvn clean package -DskipTests` | Build without running tests |
| `mvn dependency:tree` | Show dependency tree |
| `mvn help:effective-pom` | Show effective POM configuration |

---

## 🔍 Build Information

**Proxy Plugin:**
- **Artifact ID**: velocitynexus
- **Version**: 1.0.0
- **Java Version**: 17
- **Main Dependencies**: Velocity API 3.4.0-SNAPSHOT, TOML4J 0.7.2, Gson 2.10.1

**Backend Plugin:**
- **Artifact ID**: velocitynexus-backend
- **Version**: 1.0.0
- **Java Version**: 17
- **Main Dependencies**: Spigot API 1.20.1-R0.1-SNAPSHOT, Gson 2.10.1

---

## 💡 Tips

- **Use `mvn clean` before each build** to ensure no old files cause issues
- **Backend must be built separately** - it has its own `pom.xml` in `backend/` directory
- **Check console output** for any warnings or errors
- **Both JARs are required** - the GUI won't work without the backend plugin

---

**Build time**: Approximately 2-5 seconds per plugin on modern hardware  
**Total build time**: ~5-10 seconds for both plugins

**Questions?** Check [README.md](README.md) for more information.
