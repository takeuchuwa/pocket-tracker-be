# pocket-tracker-be
Pocket tracker application back end side for 
finance management.

[1. Discovery Service](#1-discovery-service)  
[2. Api Gateway](#2-api-gateway)

# 1. Discovery Service

## Docker name: discovery-service

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
<a href="http://localhost:8761/" target="_blank">
localhost:8761
</a>

# 2. Api Gateway

## Docker name: api-gateway

Entry point of application to direct user to proper service.

Api gateway aims to provide a simple, 
yet effective way to route to APIs and provide 
cross cutting concerns to them such as: security, 
monitoring/metrics, and resiliency.

**Technologies: Spring Boot, Spring Cloud Gateway**

### How to start up

1. Build service
   > docker-compose build api-gateway

   (use --no-cache if docker cache some processes after pull)

   > docker-compose build --no-cache api-gateway

2. Start up service

   > docker-compose up -d api-gateway

3. **Api Gateway** depends on 
**[Discovery service](#1-discovery-service)**, so when
you'll try to start it, discovery service will be built 
and started also automatically.
