package vn.com.greencraze.order.repository.specification;

import org.springframework.data.jpa.domain.Specification;
import vn.com.greencraze.commons.specification.BaseSpecification;
import vn.com.greencraze.order.entity.Order;
import vn.com.greencraze.order.enumeration.OrderStatus;

public class OrderSpecification extends BaseSpecification<Order> {

    public Specification<Order> filterableByStatus(OrderStatus status) {
        return (root, query, cb) ->
                (status != null)
                        ? cb.and(cb.equal(root.get("status"), status))
                        : null;
    }

    public Specification<Order> filterableByUser(String userId) {
        return (root, query, cb) ->
                (userId != null)
                        ? cb.and(cb.equal(root.get("userId"), userId))
                        : null;
    }

}
