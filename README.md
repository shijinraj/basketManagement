# wayfair - MVP (2-3 hours of work)
## Setup
*   Docker
*   JDK 8 or higher version
*   Maven 3.5.X
*   Run application using command line from the project directory - mvn spring-boot:run
    or docker run -p 127.0.0.1:8080:8080/tcp shijinraj/basket
*   Shopping-app Swagger URL- http://localhost:8080/swagger-ui/index.html

## 1. create a Basket:
*  API - HTTP : PUT - /api/baskets
*  Accepting customerId in the Request Header.
*  Accepting productId and quantity in the Request Parameter.
*  Null Validation check for customerId, productId and quantity in Service Layer.
*  Throws IllegalArgumentException for invalid productId or customerId in Service Layer.
*  This service creates a basket with Item for a user.

## 2. Update a Basket:
*  API - HTTP : PUT - /api/baskets/{id}
*  Accepting customerId in the Request Header.
*  Accepting basketId in the Path Parameter.
*  Accepting productId and quantity in the Request Parameter.
*  Null Validation check for customerId, productId and quantity in Service Layer.
*  Throws IllegalArgumentException for invalid productId or customerId in Service Layer.
*  This service updates a basket item Or add new item for a user.


## 2. Delete a Basket:
*  API - HTTP : PUT - /api/baskets/{id}
*  Accepting customerId in the Request Header.
*  Accepting basketId in the Path Parameter.
*  Accepting productId and quantity in the Request Parameter.
*  Quantity is a optional Request Parameter.
*  An Item will be completely removed from a basket if quantity is passed as null.
*  An Item quantity will be reduced if quantity is passed as non-null.   
*  Null Validation check for customerId, productId and quantity in Service Layer.
*  Throws IllegalArgumentException for invalid productId or customerId in Service Layer.
*  Throws IllegalArgumentException if available quantity is less than delete quantity in Service Layer.   
*  This service updates a basket item Or add new item for a user.

## Application Features
*   Spring Boot project with Spring Boot, Spring Dev tools, Spring Cache, Spring Data JPA, H2 DB JUnit 5 and Swagger
*   Centralised Exception handling using @RestControllerAdvice - please refer the package com.wayfair.products.exception
*   Dockerized app
 

## Assumption
*   Basket is created once per a user, and it will be available until checkout operation.
*   OneToOne relationship between Basket and User entity.    
*   Soft delete to be implemented for a Basket upon checkout.  So there will be only one active basketId available for a user at any time.
*   Basket can contain zero or more items.
*   OneToMany relationship between Basket and items.  
*   Product is the master table for all available products with id, name and price.
*   Item is having OneToOne relationship with Product.
*   Product id is the foreign primary key in an Item.
*   This is the possible solution fo  2-3 hours of work.Services layer tests has started.
