package vn.com.greencraze.commons.specification;

import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class BaseSpecification<T> {

    public Specification<T> searchable(List<String> searchFields, String search) {
        return (root, query, cb) -> {
            if (searchFields != null && !searchFields.isEmpty() && search != null && !search.isEmpty()) {
                List<Predicate> predicates = new ArrayList<>();

                for (String field : searchFields) {
                    Path<String> path = root.get(field);
                    Predicate predicate = cb.like(cb.lower(path), "%" + search.toLowerCase() + "%");
                    predicates.add(predicate);
                }

                return cb.or(predicates.toArray(new Predicate[0]));
            }

            return cb.conjunction();
        };
    }

    public Specification<T> sortable(Boolean isSortAscending, String columnName) {
        return (root, query, cb) -> {
            if (isSortAscending != null && columnName != null) {
                Path<Object> path = root.get(columnName);
                Order order = isSortAscending ? cb.asc(path) : cb.desc(path);
                query.orderBy(order);
            }

            return cb.conjunction();
        };
    }

    public Specification<T> filterable(Boolean status) {
        return (root, query, cb) ->
                (status != null && status)
                        ? cb.and(cb.equal(root.get("status"), true))
                        : null;
    }

}
