# REST Assured Travel Planner

Brief description of this travel planner

## About the application

Features

## Prerequisites

Components required to run this application

## How to run the application

Step-by-step instructions of how to run this program

Step 1: Run the service in a terminal, for instance, Flight Service - mvn spring-boot:run -pl flights

Step 2: Run in a separate terminal, the Travel Agent - mvn spring-boot:run "-Dspring-boot.run.arguments=http://localhost:8081/flights" -pl travel_agent

Step 3: Run in another separate terminal, the Client - mvn spring-boot:run -pl client 
(The URL was that being passed into this command as an argument is currently hardcoded into Client.java)

Step 4: When the Client application is running, open a new browser and enter into the url the following - http://localhost:8082/

Step 5: Enter your travel details into displayed form

### Technologies used

All technologies and methodologies used to build this application (eg. Maven, REST, ...)

### Authors

- [Seán Conor McLoughlin](https://gitlab.com/Conchobar)
- [Tanmay Joshi](https://gitlab.com/T_J)
- [Barry Murphy](https://gitlab.com/murphybt)
- [Ee En Goh](https://gitlab.com/GohEeEn)
- [Ciaran Conlon](https://gitlab.com/ciaran.conlon.1)
- [Olanipekun Akintola](https://gitlab.com/Akintola)

### Notable Resources

Resources referenced to build this application (eg. API documentations)

Note for Barry: There is a rate limit of 5 requests per minute for skyscanner 
