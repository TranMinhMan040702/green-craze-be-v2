package vn.com.greencraze.order.config.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("app")
public record AppProperties(
        long accessTokenExpirationMillis,
        long refreshTokenExpirationMillis,
        long otpTokenExpirationMillis,
        String issuer,
        String signingKey,
        String clientId
) {
}
