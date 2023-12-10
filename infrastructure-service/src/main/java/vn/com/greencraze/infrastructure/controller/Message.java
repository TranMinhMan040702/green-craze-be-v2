package vn.com.greencraze.infrastructure.controller;

public record Message(
        String senderName,
        String receiverName,
        String message,
        String date,
        Status status
) {
    public enum Status {
        JOIN,
        MESSAGE,
        LEAVE
    }
}
