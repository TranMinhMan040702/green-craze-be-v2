package vn.com.greencraze.auth.config.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.com.greencraze.auth.config.property.AppProperties;
import vn.com.greencraze.auth.entity.Identity;
import vn.com.greencraze.auth.entity.Role;
import vn.com.greencraze.auth.exception.TokenCreationException;
import vn.com.greencraze.auth.exception.TokenValidationException;

import java.time.Instant;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtManager {

    private final AppProperties appProperties;

    public String generateToken(@NotNull Identity identity) {
        try {
            Instant now = Instant.now();

            return JWT.create()
                    .withIssuer(appProperties.issuer())
                    .withSubject(identity.getId())
                    .withIssuedAt(now)
                    .withExpiresAt(now.plusMillis(appProperties.accessTokenExpirationMillis()))
                    .withJWTId(UUID.randomUUID().toString())
                    .withClaim("userId", identity.getId())
                    .withClaim("email", identity.getUsername())
                    .withClaim(
                            "roles",
                            identity.getRoles().stream().map(Role::getName).collect(Collectors.toList())
                    )
                    .sign(getAlgorithm());
        } catch (Exception e) {
            throw new TokenCreationException("Unable to create JWT");
        }
    }

    public DecodedJWT validateToken(@NotNull String token) {
        try {
            return JWT.require(getAlgorithm())
                    .withIssuer(appProperties.issuer())
                    .build()
                    .verify(token);
        } catch (Exception e) {
            throw new TokenValidationException("Invalid JWT");
        }
    }

    @NotNull
    private Algorithm getAlgorithm() {
        return Algorithm.HMAC256(appProperties.signingKey());
    }

}
