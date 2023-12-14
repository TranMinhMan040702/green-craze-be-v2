package vn.com.greencraze.infrastructure.config.cloudinary;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("cloudinary")
public record CloudinaryProperties(
        String cloudName,
        String apiKey,
        String apiSecret,
        Boolean secure
) {}
