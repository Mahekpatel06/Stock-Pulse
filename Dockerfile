#	Dockerfile-3 - Improve Layer Caching

#		FROM maven:3.9.11-amazoncorretto-25-alpine AS build
#		WORKDIR /home/app
		
#		COPY ./pom.xml /home/app/pom.xml
#		COPY ./src/main/java/com/in28minutes/rest/webservices/restfulwebservices/RestfulWebServicesApplication.java	  /home/app/src/main/java/com/in28minutes/rest/webservices/restfulwebservices/RestfulWebServicesApplication.java
		
#		RUN mvn -f /home/app/pom.xml clean package
		
#		COPY . /home/app
#		RUN mvn -f /home/app/pom.xml clean package
		
#		FROM eclipse-temurin:25-jre-alpine
#		EXPOSE 5000
#		COPY --from=build /home/app/target/*.jar app.jar
		
		
        # We need the JDK (not JRE) for DevTools to work properly
#        FROM eclipse-temurin:21-jdk-alpine
        
 #       WORKDIR /app
        
        # We don't need to COPY or RUN MVN here because 
        # the Bind Mount will provide the compiled files from Eclipse.
        
  #      EXPOSE 8888
        
        # This runs the compiled classes directly from the mounted target folder
   #     ENTRYPOINT ["java", "-cp", "target/classes:target/dependency/*", "com.ownProject.GINS.GinsApplication"]
   
   
   
   # Stage 1: Build the application
   FROM maven:3.9.6-eclipse-temurin-21-alpine AS build
   WORKDIR /app
   COPY . .
   RUN mvn clean package -DskipTests

   # Stage 2: Run the application
   FROM eclipse-temurin:21-jdk-alpine
   WORKDIR /app
   # Copy the built jar from the first stage
   COPY --from=build /app/target/*.jar app.jar

   EXPOSE 10000

   ENTRYPOINT ["java", "-jar", "app.jar", "--server.port=10000"]

