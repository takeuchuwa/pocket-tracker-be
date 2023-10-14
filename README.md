# pocket-tracker-be
Pocket tracker application back end side for 
finance management.

# Content

[1. Description](#1-description)  
[2. Technologies](#2-technologies)  
[3. Services](#3-services)  
&nbsp;&nbsp;&nbsp;&nbsp;[3.1 Discovery Service](#31-discovery-service)  
&nbsp;&nbsp;&nbsp;&nbsp;[3.2 Api Gateway](#32-api-gateway)  
[4. Non-service containers](#4-non-service-containers)  
&nbsp;&nbsp;&nbsp;&nbsp;[4.1 PostgreSQL](#41-postgresql-users-db)  
&nbsp;&nbsp;&nbsp;&nbsp;[4.2 Redis](#42-redis-user-db)  
&nbsp;&nbsp;&nbsp;&nbsp;[4.3 Vault](#43-vault)  

# 1. Description

This is BE-side of pocket tracker project which made for finance management.

BE-side development team:  
[Dmytro Horban](https://www.linkedin.com/in/takeuchuha/)

# 2. Technologies

## Core:
* Java
* Spring Boot

### Cloud:
* Spring Cloud Gateway
* Spring Web Flux

### DB:
* Spring JPA
* Redis
* PostgreSQL

### Security:
* Spring Security
* JWT

# 3. Services

All services you can start up by using docker-compose
> docker-compose up

All dependencies set and each service 
will be start when other container 
on which it depends is started.

## 3.1 Discovery Service

### Docker name: discovery-service

Service that has information about other services with 
their registered names. It helps to resolve host names 
between services which ease BE development.

A service registry is useful because it enables 
client-side load-balancing and decouples service 
providers from consumers without the need for DNS.

**Technologies: Spring Boot, Spring Eureka Server**

### How to start up

It's really easy to start up service with docker.
Just open root folder and type this commands.

1. Build service
    > docker-compose build discovery-service

    (use --no-cache if docker cache some processes after pull)
    
    > docker-compose build --no-cache discovery-service

2. Start up service
    
    > docker-compose up -d discovery-service

After start, you can access it on 
[localhost:8761](http://localhost:8761/)

## 3.2 Api Gateway

### Docker name: api-gateway

Entry point of application to direct user to proper service.

Api gateway aims to provide a simple, 
yet effective way to route to APIs and provide 
cross-cutting concerns to them such as: security, 
monitoring/metrics, and resiliency.

Each next service you can access through [localhost](http://localhost/)

**Technologies: Spring Boot, Spring Cloud Gateway**

### How to start up

1. Build service
   > docker-compose build api-gateway

   (use --no-cache if docker cache some processes after pull)

   > docker-compose build --no-cache api-gateway

2. Start up service

   > docker-compose up -d api-gateway

3. **Api Gateway** depends on 
**[Discovery service](#31-discovery-service)**, so when
you'll try to start it, discovery service will be built 
and started also automatically.

## 3.3 User service

### Docker name: service-user

Main service for sign up and login.

**Technologies: Spring Boot, Spring Security, JWT, Spring JPA, Spring Redis**

### How to start up

1. Build service
   > docker-compose build service-user

   (use --no-cache if docker cache some processes after pull)

   > docker-compose build --no-cache service-user

2. Start up service

   > docker-compose up -d service-user

3. **User Service** depends on
   **[Discovery service](#31-discovery-service)**, 
   **[PostgreSQL](#41-postgresql-users-db)**,
   **[Redis](#42-redis-user-db)** and
   **[Vault](#43-vault)**, so when
   you'll try to start it, discovery service will be built
   and started also automatically.

4. After start open API should be available on: 
[localhost:8081/swagger-ui/](http://localhost:8081/swagger-ui/index.html#/).

# 4. Non service containers

There are will be other staff like DB's, Clouds, etc.

## 4.1 PostgreSQL Users DB

### Docker name: users-db

Used for store usage. User service communicates through Spring JPA.
Credentials management through [Vault](#43-vault)

## 4.2 Redis User DB

### Docker name: jwt-rt-redis-db

Key-value pair database. Used for storage jwt and refresh tokens pair.
Credentials management through [Vault](#43-vault)

## 4.3 Vault

### Docker name: vault-server

Cloud service to manage secrets. 
Now stores key-value pairs for different applications 
and manage user credentials 
by creating user for some amount of time to access database.

Don't have volume for dev env. 
Docker container **vault-client** initialize all needed secrets.

