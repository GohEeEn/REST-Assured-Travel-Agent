package service.flights;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
// import org.springframework.cloud.client.ServiceInstance;
// import org.springframework.cloud.client.discovery.DiscoveryClient;
// import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
// import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
// import org.springframework.cloud.netflix.eureka;
// import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

import java.util.List;

// @EnableEurekaClient
@SpringBootApplication
public class Application {

      @Bean
    //   @LoadBalanced
      public RestTemplate restTemplate(){
            return new RestTemplate();
}

      public static void main(String[] args) {
            SpringApplication.run(Application.class, args);
      }
} 