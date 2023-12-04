package vn.com.greencraze.infrastructure.enumeration;

public enum EmailEvent {
    CONFIRM_REGISTRATION("confirm-registration-mail-template", "[Green Craze System] Xác nhận đăng ký tài khoản"),
    FORGOT_PASSWORD("forgot-password-mail-template", "[Green Craze System] Đặt lại mật khẩu"),
    ORDER_CONFIRMATION("order-confirmation-mail-template", "[Green Craze System] Xác nhận đơn hàng"),
    RESEND_OTP("resend-otp-mail-template", "[Green Craze System] Gửi lại mã");

    private final String template;

    private final String subject;

    EmailEvent(String template, String subject) {
        this.template = template;
        this.subject = subject;
    }

    public String template() {
        return this.template;
    }

    public String subject() {
        return this.subject;
    }
}
