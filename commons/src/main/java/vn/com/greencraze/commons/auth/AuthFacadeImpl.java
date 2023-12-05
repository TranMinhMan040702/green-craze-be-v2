package vn.com.greencraze.commons.auth;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class AuthFacadeImpl implements AuthFacade {

    @Override
    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    @Override
    public boolean isAuthenticated() {
        return getAuthentication().isAuthenticated();
    }

    @Override
    public String getUserId() {
        Authentication authentication = getAuthentication();

        if (authentication instanceof AnonymousAuthenticationToken) {
            throw new AuthenticationCredentialsNotFoundException("Unable to retrieve user ID due to anonymous access");
        } else {
            try {
                return authentication.getName().split(",")[0];
            } catch (Exception e) {
                throw new AuthenticationCredentialsNotFoundException(
                        "Unable to retrieve user ID while parsing text: %s".formatted(authentication.getName()));
            }
        }
    }

    @Override
    public String getUserAccessToken() {
        Authentication authentication = getAuthentication();

        if (authentication instanceof AnonymousAuthenticationToken) {
            throw new AuthenticationCredentialsNotFoundException("Unable to retrieve user ID due to anonymous access");
        } else {
            try {
                return authentication.getName().split(",")[1];
            } catch (Exception e) {
                throw new AuthenticationCredentialsNotFoundException(
                        "Unable to retrieve user ID while parsing text: %s".formatted(authentication.getName()));
            }
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Authentication authentication = getAuthentication();

        if (authentication instanceof AnonymousAuthenticationToken) {
            throw new AuthenticationCredentialsNotFoundException("Unable to retrieve authorities due to anonymous access");
        } else {
            try {
                return authentication.getAuthorities();
            } catch (Exception e) {
                throw new AuthenticationCredentialsNotFoundException(
                        "Unable to retrieve authorities while parsing text: %s".formatted(authentication.getName()));
            }
        }
    }

}
