# Rental Cars
>An application supporting the management of rental cars

## Table of contents
* [General info](#general-info)
* [Technologies](#technologies)
* [Setup](#setup)
* [Features](#features)
* [Status](#status)
* [Contact](#contact)

## General info
The purpose of creating this application was to put into practice the knowledge 
I had accumulated so far about creating database REST applications using Spring Framework.
The main functionality of the application is to allow customers to view the list
of available cars and their details, and then book the selected car. 
There are also endpoints for administrator that gives possibilities, among other things, order management, adding and editing cars.

## Technologies
#### The REST API is written in Java 9 using the Spring framework and following technologies:
* Maven
* Spring Boot
* Spring MVC
* Spring Data JPA
* Spring Security
* JUnit
* Liquibase
* Lombok
* Mapstruct
* Swagger

## Setup
#### To run you will need:
* JDK 9 (or higher version)
* Maven
* PostgreSQL (with default port: 5432; Create database with name "rentalcars")

### There are two ways to start the application:
#### 1) To start the application with default profile 'prod' (connection with PosgreSQL Data Base) write command:
```
mvn spring-boot:run
```
#### 2) To start the application with 'dev' profile (connection with H2 Data Base which have sample records) write command:
```
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```
## Features
#### To explore features start application and open your browser with following url: 
```
localhost:8080/swagger-ui.html
```
#### You can log in as an Administrator:
```
Username: admin
```
```
Password: admin
```
#### or as a regular User (sample record in 'dev' profile):
```
Username: nowak@poczta.pl
```
```
Password: password
```
#### To-do List:
* Searching and sorting cars according to given criteria
* Front-end

## Status
Project is in progress.

## Contact
Created by r.kuszlewicz@gmail.com - feel free to contact me!
