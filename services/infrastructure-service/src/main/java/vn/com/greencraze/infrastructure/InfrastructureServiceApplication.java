package vn.com.greencraze.infrastructure;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = "vn.com.greencraze")
@EnableFeignClients(basePackages = "vn.com.greencraze.infrastructure.client")
public class InfrastructureServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(InfrastructureServiceApplication.class, args);
    }

}
