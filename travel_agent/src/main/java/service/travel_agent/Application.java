package service.travel_agent;

import service.travel_agent.TravelAgentService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
      public static void main(String[] args) {
            for(String arg:args) {
                  TravelAgentService.URIs.add(arg);
            }
            SpringApplication.run(Application.class, args);
      }
} 
