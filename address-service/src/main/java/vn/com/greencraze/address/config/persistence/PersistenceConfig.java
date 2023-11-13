package vn.com.greencraze.address.config.persistence;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import vn.com.greencraze.commons.config.persistence.CommonPersistenceConfig;

@Configuration
@EnableJpaAuditing
public class PersistenceConfig implements CommonPersistenceConfig {}
