# Sokha Mart POS

Multi-tenant retail POS system for Sokha Mart, built as Spring Boot microservices.

## Prerequisites
- JDK 25
- Maven 3.9+

## Build and test
```
mvn test
```

## Run the service template
```
mvn -f services/service-template/pom.xml spring-boot:run
curl http://localhost:8080/api/v1/ping   # {"message":"pong"}
```

## Layout
- `platform/` shared platform libraries (`platform-parent` holds Java and dependency versions)
- `services/` Spring Boot services (`service-template` is the starting point for new services)
- `docs/` teaching plan, progress, ADRs
