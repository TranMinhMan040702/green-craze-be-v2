package vn.com.greencraze.user.client.auth.dto.request;

import lombok.Builder;

import java.util.List;

@Builder
public record DisableListIdentityRequest(
        List<String> ids
) {}
