package vn.com.greencraze.address.repository.specification;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import vn.com.greencraze.address.entity.Address;
import vn.com.greencraze.commons.specification.BaseSpecification;

import java.util.ArrayList;
import java.util.List;

public class AddressSpecification extends BaseSpecification<Address> {

    public Specification<Address> filterable(String userId) {
        List<Predicate> wheres = new ArrayList<>();
        return (root, query, cb) -> {
            if (userId != null) {
                Predicate isEqualUserId = cb.equal(root.get("userId"), userId);
                wheres.add(isEqualUserId);
            }
            return cb.and(wheres.toArray(new Predicate[0]));
        };
    }

}
