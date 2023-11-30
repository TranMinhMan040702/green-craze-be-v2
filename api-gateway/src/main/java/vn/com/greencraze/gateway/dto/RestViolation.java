package vn.com.greencraze.gateway.dto;

import java.util.List;

public record RestViolation(
        String field,
        List<String> messages
) {}
