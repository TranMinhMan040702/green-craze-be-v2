package vn.com.greencraze.commons.advice;

import java.util.List;

public record RestViolation(
        String field,
        List<String> messages
) {}
