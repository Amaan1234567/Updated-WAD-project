from eclipse-temurin:21-jre-ubi9-minimal

arg JAR_FILE=target/*.jar
workdir /opt/plant.io/api-gateway

copy ${JAR_FILE} ./api-gateway.jar

expose 8080

run useradd api-gateway
user api-gateway

cmd ["java","-jar","api-gateway.jar"]