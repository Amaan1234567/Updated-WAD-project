from eclipse-temurin:21-jre-ubi9-minimal

arg JAR_FILE=target/*.jar
workdir /opt/plant.io/product-service

copy ${JAR_FILE} ./product-service.jar

expose 8081

run useradd product-service
user product-service

cmd ["java","-jar","product-service.jar"]