package vn.com.greencraze.meta.config;

import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

@Configuration
@RefreshScope
@ConfigurationPropertiesScan
public class AppConfig {
}
