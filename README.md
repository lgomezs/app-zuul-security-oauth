# app-zuul-security-oauth

 ![Screenshot from running application](img/zuul.png?raw=true "Screenshot zuul")
 
Routing is an integral part of a microservice architecture. 
For example,  may be mapped to your web application: /api/users is mapped to the user service and /api/shop is mapped to the shop service. 
Zuul is a JVM-based router and server-side load balancer from Netflix.

Technology Used:
  - Spring boot 2.1.7.RELEASE
  - Zuul API Gateway
  - Spring Security for the oauth server
  - openfeign
  - lombok

# Application

 We will use an API gateway implemented using the Spring Cloud Netfix Zuul server to route requests to our microservices.
 To perform the single sign-on for all our services, we will currently use an OAuth2 server configured with Spring Cloud Security.

    Modulos:  
    - app-security-zuul
    - app-support-security
    
# Build & Run
        
   ## app-security-zuul (gateway)
   
  Service gateway and filter that validate token.
      
    cd app-security-zuul
    mvn clean package
    mvn clean install
      
   ###  Deploy with docker compose
   
    cd app-security-zuul\src\main\docker
    docker-compose up
    
   If we try to connect to the support service first valid to the user through the oauth service.
        
     POST : http://localhost:8085/support
        
   ![Screenshot from running application](img/zuul-security.png?raw=true "Screenshot zuul")
    
        
   ## app-support-security (oauth server) 
   
    http://support:8080/oauth/check_token
    
    add line to file hosts.conf:
        
      support  localhost
    
    
    