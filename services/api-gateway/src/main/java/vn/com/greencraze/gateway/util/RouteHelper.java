package vn.com.greencraze.gateway.util;

import org.springframework.cloud.gateway.route.RouteDefinition;
import vn.com.greencraze.gateway.dto.SimpleRouteDefinition;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RouteHelper {

    public static SimpleRouteDefinition mapToSimpleRouteDefinition(RouteDefinition routeDefinition) {
        String routeId = routeDefinition.getId();
        String regex = "(?<authLevel>.+)::(?<serviceCode>.+)::(?<apiName>.+)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(routeId);

        String authLevel, serviceCode, apiName;

        if (matcher.find()) {
            authLevel = matcher.group("authLevel");
            serviceCode = matcher.group("serviceCode");
            apiName = matcher.group("apiName");
        } else {
            throw new RuntimeException("Invalid route ID");
        }

        String method = routeDefinition.getPredicates()
                .stream()
                .filter(predicateDefinition -> predicateDefinition.getName().equals("Method"))
                .findFirst()
                .map(predicateDefinition -> predicateDefinition.getArgs().get("_genkey_0"))
                .orElseThrow(() -> new RuntimeException("Unable to retrieve method from route"));

        String sourcePath = routeDefinition.getPredicates()
                .stream()
                .filter(predicateDefinition -> predicateDefinition.getName().equals("Path"))
                .findFirst()
                .map(predicateDefinition -> predicateDefinition.getArgs().get("_genkey_0"))
                .orElseThrow(() -> new RuntimeException("Unable to retrieve source path from route"));

        String targetPath = routeDefinition.getFilters()
                .stream()
                .filter(filterDefinition -> filterDefinition.getName().equals("SetPath"))
                .findFirst()
                .map(filterDefinition -> filterDefinition.getArgs().get("_genkey_0"))
                .orElseThrow(() -> new RuntimeException("Unable to retrieve target path from route"));

        return SimpleRouteDefinition.builder()
                .id(SimpleRouteDefinition.ID.builder()
                        .raw(routeId)
                        .authLevel(authLevel)
                        .serviceCode(serviceCode)
                        .apiName(apiName)
                        .build())
                .targetBaseUri(routeDefinition.getUri().toString())
                .method(method)
                .sourcePath(sourcePath)
                .targetPath(targetPath)
                .build();
    }

}
