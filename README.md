# spring-filter-stream-copier

### spring-boot HTTP-body-stream copier in a Request.

- java-version: 17
- gradle-version: 8.8
- spring-boot: 3.x


### Build Gradle

#####   
- on Window Platform. build without testing

```bash
cd <PROJECT-DIR>
set JAVA_HOME=<JAVA-17 or higher home directory>
set GRADLE_HOME=<Your gradle home directory. default on C:\Users\[your-user-name]\.gradle>
gradlew.bat clean build -Dspring.output.ansi.enabled=ALWAYS -Dskip.tests ^
-Dorg.gradle.java.home=%JAVA_HOME% ^
-Dgradle.user.home=%GRADLE_HOME% ^
-x test
```

#####   

- on Linux Platform build without testing

```bash
cd <PROJECT-DIR>
JAVA_HOME=<JAVA-17 or higher home directory>
GRADLE_HOME=<Your gradle home directory.>
gradlew clean build -Dspring.output.ansi.enabled=ALWAYS -Dskip.tests \
-Dorg.gradle.java.home=${JAVA_HOME} \
-Dgradle.user.home=${GRADLE_HOME} \
-x test
```


### Laucher using java-17 or higher

```bash
java -jar spring-filter-stream-copier.jar
```

### Testing with Postman   

- import postman project: **/test/postman/spring-filter-stream-copier.postman_collection.json**   

- Test your requests   

