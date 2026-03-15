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
		
		
		FROM maven:3.9.12-amazoncorretto-25-alpine AS build
		WORKDIR /home/app

		# 1. Copy only pom.xml to download dependencies (this is the "heavy" part)
		COPY ./pom.xml /home/app/pom.xml
		RUN mvn -f /home/app/pom.xml dependency:go-offline 

		# 2. Copy the rest of the source code
		COPY ./src /home/app/src

		# 3. Build the actual jar
		RUN mvn -f /home/app/pom.xml clean package

		FROM eclipse-temurin:25-jre-alpine
		EXPOSE 5000
		COPY --from=build /home/app/target/*.jar app.jar
		ENTRYPOINT ["java", "-jar", "/app.jar"]
