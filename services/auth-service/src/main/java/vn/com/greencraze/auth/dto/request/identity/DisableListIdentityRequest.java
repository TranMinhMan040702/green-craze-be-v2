package vn.com.greencraze.auth.dto.request.identity;

import java.util.List;

public record DisableListIdentityRequest(
        List<String> ids
) {}
