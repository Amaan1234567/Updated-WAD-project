mvn package -DskipTests
docker build -t plantio1/api-gateway:latest api-gateway
docker build -t plantio1/discovery-server:latest discovery-server
docker build -t plantio1/frontend:latest frontend
docker build -t plantio1/orders-service:latest orders-service
docker build -t plantio1/products-service:latest products-service
docker build -t plantio1/users-service:latest users-service