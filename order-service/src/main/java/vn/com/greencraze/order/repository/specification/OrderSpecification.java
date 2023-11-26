package vn.com.greencraze.order.repository.specification;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import vn.com.greencraze.commons.specification.BaseSpecification;
import vn.com.greencraze.order.entity.Order;

import java.util.ArrayList;
import java.util.List;

public class OrderSpecification extends BaseSpecification<Order> {

    public Specification<Order> filterable(String userId, String status) {
        List<Predicate> wheres = new ArrayList<>();
        return (root, query, cb) -> {
            if (userId != null) {
                Predicate isEqualUserId = cb.equal(root.get("userId"), userId);
                wheres.add(isEqualUserId);
            }
            if (status != null) {
                Predicate isEqualStatus = cb.equal(root.get("status"), status);
                wheres.add(isEqualStatus);
            }
            return cb.and(wheres.toArray(new Predicate[0]));
        };
    }

}
