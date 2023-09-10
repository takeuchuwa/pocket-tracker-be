# pocket-tracker-be
Pocket tracker application back end side for 
finance management.

[1. Discovery Service](#1-discovery-service)

<h1 id="1-discovery-service">1. Discovery Service</h1>

## Docker name: discovery-service

Service that has information about other services with 
their registered names. It helps to resolve host names 
between services which ease BE development.

A service registry is useful because it enables 
client-side load-balancing and decouples service 
providers from consumers without the need for DNS.

**Technologies: Spring Boot, Spring Eureka Server**

### How to start up

It's really easy to start up application with docker.
Just open root folder and type this commands.

1. Build service
    > docker-compose build discovery-service
   > 
    (use --no-cache if docker cache some processes after pull)
    
    > docker-compose build --no-cache discovery-service

2. Start up service
    
    > docker-compose up -d discovery-service

