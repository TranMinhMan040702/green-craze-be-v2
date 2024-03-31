package vn.com.greencraze.infrastructure.repository.specification;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import vn.com.greencraze.infrastructure.entity.Room;

@Component
public class RoomSpecification {

    public Specification<Room> orderByLatestMessageUpdatedAt() {
        return (root, query, cb) -> {
            query.orderBy(cb.desc(root.join("messages").get("updatedAt")));
            return cb.conjunction();
        };
    }

}
