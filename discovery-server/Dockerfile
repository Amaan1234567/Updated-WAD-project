from eclipse-temurin:21-jre-ubi9-minimal

arg JAR_FILE=target/*.jar
workdir /opt/plant.io/discovery-server

copy ${JAR_FILE} ./discovery-server.jar

expose 8761

run useradd discovery-server
user discovery-server

cmd ["java","-jar","discovery-server.jar"]