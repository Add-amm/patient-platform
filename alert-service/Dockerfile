# -------- Build stage --------
    FROM maven:3.9.9-eclipse-temurin-17 AS build
    WORKDIR /app
    
    # Copy only pom first (better caching)
    COPY pom.xml .
    
    # Download dependencies (no debug, more stable)
    RUN mvn dependency:resolve
    
    # Copy source code
    COPY src ./src
    
    # Build the jar
    RUN mvn clean package -DskipTests
    
    # -------- Run stage --------
    FROM eclipse-temurin:17-jre
    WORKDIR /app
    
    # Copy built jar
    COPY --from=build /app/target/*.jar app.jar
    
    # Expose port
    EXPOSE 8083
    
    # Run app
    ENTRYPOINT ["java", "-jar", "app.jar"]    