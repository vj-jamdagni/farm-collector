# FarmCollector Application

FarmCollector is a Spring Boot-based application designed to collect and manage farm-related data, including information about planted and harvested crops. The application provides APIs for farmers to submit planting and harvest data and allows the organization to generate season-based reports comparing expected vs actual yields for each farm and crop type.

## Table of Contents

- [Project Overview](#project-overview)
- [Technologies](#technologies)
- [Setup](#setup)
- [APIs](#apis)
    - [1. Planting API](#1-planting-api)
    - [2. Harvest API](#2-harvest-api)
    - [3. Season Report API](#3-season-report-api)
- [Testing](#testing)
- [How to Run](#how-to-run)
- [License](#license)

## Project Overview

The FarmCollector application handles:
1. **Planted Crops Data**: Farmers submit data about crops they have planted (including planting area and expected yield).
2. **Harvested Crops Data**: Farmers submit data about the actual amount of harvested crops.
3. **Season Reports**: The system generates reports for each season, comparing the expected and actual yields for each farm and each type of crop.

## Technologies

- **Java 21**
- **Spring Boot 3.x**
- **Spring Data JPA** (Hibernate)
- **H2 Database** (In-memory database for development/testing)
- **JUnit 5** for unit testing
- **Mockito** for mocking
- **Maven** for project build and dependency management

## Setup

### Prerequisites

- Java 21
- Maven 3.x

## Build and run the application
```Bash
mvn clean install
mvn spring-boot:run
```

The application will start on http://localhost:8080

## H2 Console
You can access the in-memory H2 database via the H2 console at http://localhost:8080/h2-console. 
The default JDBC URL is jdbc:h2:mem:testdb.

## APIs

### Planting API
This API is used to submit data about crops that are planted.

#### cURL
```bash
curl --location 'http://localhost:8080/v1/api/farm/123/plantings' \
--header 'Content-Type: application/json' \
--data '{
    "season" : "SPRING",
    "farmName": "MyFarm101",
    "crops": [
        {
            "name": "corn",
            "areaInAcre": 7,
            "expectedYield": 25
        },
        {
            "name": "wheat",
            "areaInAcre": 12,
            "expectedYield": 57
        }
    ]
}'
```
Response : 200 OK
```
Plantation details successfully added.
```

### Harvest API
This API is used to submit data about harvested crops.

#### cURL
```bash
curl --location 'http://localhost:8080/v1/api/farm/123/harvests' \
--header 'Content-Type: application/json' \
--data '{
  "season": "SPRING",
  "crops": [
    {
      "name": "corn",
      "actualYield": 18
    },
    {
      "name": "wheat",
      "actualYield": 12
    }
  ]
}
'
```

Response 200 Ok
```
Harvest data added.
```

### Season Report API
This API generates a report for a given season, showing expected vs actual yield for each farm and crop type.

```bash
curl --location 'http://localhost:8080/v1/api/reports/seasons/SPRING'
```

Response : 200 Ok
```
{
    "season": "SPRING",
    "reports": [
        {
            "farmName": "MyFarm101",
            "crops": [
                {
                    "name": "corn",
                    "expectedYield": "25.0",
                    "actualYield": "18.0"
                },
                {
                    "name": "wheat",
                    "expectedYield": "57.0",
                    "actualYield": "12.0"
                }
            ]
        }
    ]
}
```


## Testing
This project includes unit tests for both the service and controller layers. The service tests ensure that the business logic is working correctly, while the controller tests check that the API endpoints behave as expected.
The tests can be run manually using maven

```
mvn test
```