package vn.com.greencraze.infrastructure.enumeration;

public enum EmailEvent {
    CHANGE_PASSWORD("change-password-mail-template", "[Green Craze System] Thay đổi mật khẩu");

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
