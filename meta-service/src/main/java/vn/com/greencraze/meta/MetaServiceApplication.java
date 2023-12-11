package vn.com.greencraze.meta;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = "vn.com.greencraze")
@EnableFeignClients(basePackages = "vn.com.greencraze.meta.client")
public class MetaServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MetaServiceApplication.class, args);
    }

}
