package vn.com.greencraze.user.dto.response.review;

import lombok.With;

import java.time.Instant;

@With
public record GetTop5ReviewLatest(
        Long id,
        Instant createdAt,
        String title,
        String productName,
        Integer rating
) {}
