package vn.com.greencraze.address.repository.specification;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import vn.com.greencraze.address.entity.Address;
import vn.com.greencraze.commons.specification.BaseSpecification;

import java.util.ArrayList;
import java.util.List;

public class AddressSpecification extends BaseSpecification<Address> {

    public Specification<Address> filterable(String userId, Boolean status) {
        List<Predicate> wheres = new ArrayList<>();
        return (root, query, cb) -> {
            if (userId != null) {
                Predicate isEqualUserId = cb.equal(root.get("userId"), userId);
                wheres.add(isEqualUserId);
            }
            if (status != null) {
                Predicate isEqualStatusId = cb.equal(root.get("status"), status);
                wheres.add(isEqualStatusId);
            }
            return cb.and(wheres.toArray(new Predicate[0]));
        };
    }

}
