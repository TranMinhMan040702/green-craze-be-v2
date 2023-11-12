package vn.com.greencraze.commons.util;

import io.github.perplexhub.rsql.RSQLJPASupport;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.stream.Collectors;

public interface SearchHelper {

    static <T> Specification<T> parseSearchToken(String search, List<String> searchFields) {
        if (search == null || search.isBlank() || searchFields == null || searchFields.isEmpty()) {
            return RSQLJPASupport.toSpecification((String) null);
        }

        return searchFields.stream()
                .map(field -> field + "=like='" + search.trim() + "'")
                .collect(Collectors.collectingAndThen(Collectors.joining(","), RSQLJPASupport::toSpecification));
    }

}
