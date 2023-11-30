package vn.com.greencraze.infrastructure;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "vn.com.greencraze")
public class InfrastructureServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(InfrastructureServiceApplication.class, args);
    }

}
