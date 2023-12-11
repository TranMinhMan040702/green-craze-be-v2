package vn.com.greencraze.infrastructure.repository.specification;

import org.springframework.data.jpa.domain.Specification;
import vn.com.greencraze.commons.specification.BaseSpecification;
import vn.com.greencraze.infrastructure.entity.Notification;

public class NotificationSpecification extends BaseSpecification<Notification> {

    public Specification<Notification> filterable(String userId) {
        return (root, query, cb) ->
                (userId != null)
                        ? cb.and(cb.equal(root.get("userId"), userId))
                        : null;
    }

}
