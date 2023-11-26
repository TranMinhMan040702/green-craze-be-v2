package vn.com.greencraze.commons.auth;

import org.springframework.security.core.Authentication;

public interface AuthFacade {

    Authentication getAuthentication();

    boolean isAuthenticated();

    String getUserId();

}
