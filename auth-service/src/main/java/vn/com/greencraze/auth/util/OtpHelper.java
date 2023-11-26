package vn.com.greencraze.auth.util;

import java.util.Random;

public class OtpHelper {

    public static void main(String[] args) {
        System.out.println(createOtp());
    }

    public static String createOtp() {
        long number = Long.parseLong("1" + "0".repeat(6));
        Random random = new Random();

        long result = random.nextInt() % number;
        return String.format("%0" + 6 + "d", result);
    }

}
