# -------- Build stage --------
    FROM maven:3.9.9-eclipse-temurin-17 AS build
    WORKDIR /app
    
    # Copy pom first (cache optimization)
    COPY pom.xml .
    
    # Resolve dependencies (stable)
    RUN mvn -B -q dependency:resolve
    
    # Copy source
    COPY src ./src
    
    # Build jar
    RUN mvn clean package -DskipTests
    
    # -------- Run stage --------
    FROM eclipse-temurin:17-jre
    WORKDIR /app
    
    # Copy jar dynamically
    COPY --from=build /app/target/*.jar app.jar
    
    EXPOSE 8082
    
    ENTRYPOINT ["java", "-jar", "app.jar"]
    