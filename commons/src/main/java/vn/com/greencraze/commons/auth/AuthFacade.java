package vn.com.greencraze.commons.auth;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public interface AuthFacade {

    Authentication getAuthentication();

    boolean isAuthenticated();

    String getUserId();

    Collection<? extends GrantedAuthority> getAuthorities();

}
