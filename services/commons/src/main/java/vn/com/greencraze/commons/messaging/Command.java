package vn.com.greencraze.commons.messaging;

public record Command<ID, T>(
        ID identifier,
        T payload
) {}
