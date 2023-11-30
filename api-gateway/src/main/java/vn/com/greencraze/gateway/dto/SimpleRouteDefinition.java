package vn.com.greencraze.gateway.dto;

import lombok.Builder;

@Builder
public record SimpleRouteDefinition(
        ID id,
        String targetBaseUri,
        String method,
        String sourcePath,
        String targetPath
) {

    @Builder
    public record ID(
            String raw,
            String authLevel,
            String serviceCode,
            String apiName
    ) {}

}
