package vn.com.greencraze.auth.util;

import java.security.SecureRandom;
import java.util.Base64;

public class RefreshTokenHelper {

    public static String createRefreshToken() {
        byte[] randomNumber = new byte[64];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(randomNumber);

        return Base64.getEncoder().encodeToString(randomNumber);
    }

}
