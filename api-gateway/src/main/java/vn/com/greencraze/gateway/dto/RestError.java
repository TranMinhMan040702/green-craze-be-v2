package vn.com.greencraze.gateway.dto;

import java.net.URI;
import java.util.Collections;
import java.util.List;

public record RestError(
        int status,
        URI type,
        String title,
        String detail,
        URI instance,
        String code,
        List<RestViolation> violations
) {

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private int status;
        private URI type;
        private String title;
        private String detail;
        private URI instance;
        private String code;
        private List<RestViolation> violations = Collections.emptyList();

        public Builder status(int status) {
            this.status = status;
            return this;
        }

        public Builder type(URI type) {
            this.type = type;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder detail(String detail) {
            this.detail = detail;
            return this;
        }

        public Builder instance(URI instance) {
            this.instance = instance;
            return this;
        }

        public Builder code(String code) {
            this.code = code;
            return this;
        }

        public Builder violations(List<RestViolation> violations) {
            this.violations = violations;
            return this;
        }

        public RestError build() {
            return new RestError(status, type, title, detail, instance, code, violations);
        }
    }

}
