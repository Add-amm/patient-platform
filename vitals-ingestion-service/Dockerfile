# -------- Build stage --------
    FROM maven:3.9.9-eclipse-temurin-17 AS build
    WORKDIR /app
    
    # Copy pom.xml first (for caching)
    COPY pom.xml .
    
    # Resolve dependencies (light & stable)
    RUN mvn -B -q dependency:resolve
    
    # Copy source code
    COPY src ./src
    
    # Build the application
    RUN mvn clean package -DskipTests
    
    # -------- Run stage --------
    FROM eclipse-temurin:17-jre
    WORKDIR /app
    
    # Copy generated jar
    COPY --from=build /app/target/*.jar app.jar
    
    EXPOSE 8084
    
    ENTRYPOINT ["java", "-jar", "app.jar"]
    