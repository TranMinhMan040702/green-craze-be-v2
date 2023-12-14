package vn.com.greencraze.user.config.cloudinary;

import com.cloudinary.Cloudinary;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class CloudinaryConfig {

    private final CloudinaryProperties cloudinaryProperties;

    @Bean
    public Cloudinary cloudinary() {
        Map<Object, Object> config = new HashMap<>();
        config.put("cloud_name", cloudinaryProperties.cloudName());
        config.put("api_key", cloudinaryProperties.apiKey());
        config.put("api_secret", cloudinaryProperties.apiSecret());
        config.put("secure", cloudinaryProperties.secure());
        return new Cloudinary(config);
    }

}
