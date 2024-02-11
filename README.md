# Green Craze

## Contributor
- Tran Minh Man     -     20110301
- Nguyen Minh Son   -     20110713

## Description
-  Green Craze is a project on Software Engineering. This project is an e-commerce project built
   based on microservice architecture.

![Microservice.drawio (2).png](..%2F..%2F..%2F..%2FHCMUTE%2FNam4%2FHK1%2FTieuLuanChuyenNganh%2Flogo%2FMicroservice.drawio%20%282%29.png)

## Technologies and frameworks
- Java 17.
- Spring boot 3.
- Spring Cloud Gateway, Open Feign,...
- Web socket.
- MySql
- RabbitMq.
- Docker, Docker Compose.
- Grafana Stack: Prometheus and Grafana.
- Zipkin.

## How to use:
### Requirements:
- Docker version >= 20.10.24.
- Docker compose version >= 2.17.2.
- Git version >= 2.33.0.
- Java version >= 17.
- Terminal (PowerShell).

#### Clone project:
``` bash
git clone https://github.com/TranMinhMan040702/green-craze-be-v2
```

#### Create network:
```bash
docker network create -d bridge green-craze-network
```

#### Run Infra:
```bash
docker-compose -f docker-compose.infra.yml up -d
```

> [!NOTE]  
> For Prometheus and Zipkin no configuration is required, but Grafana needs to configure username, password and 
> import dashboard.

#### Build Jar:
```bash
cd services
./mvnw clean
./mvnw clean package -fn
```

#### Run all service:
```bash
docker-compose -f docker-compose.yml up -d
```