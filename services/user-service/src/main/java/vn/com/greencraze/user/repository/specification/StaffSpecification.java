package vn.com.greencraze.user.repository.specification;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import vn.com.greencraze.commons.specification.BaseSpecification;
import vn.com.greencraze.user.entity.Staff;
import vn.com.greencraze.user.entity.UserProfile;

import java.util.ArrayList;
import java.util.List;

public class StaffSpecification extends BaseSpecification<Staff> {

    @Override
    public Specification<Staff> searchable(List<String> searchFields, String search) {
        return (root, query, cb) -> {
            if (searchFields != null && !searchFields.isEmpty() && search != null && !search.isEmpty()) {
                Join<Staff, UserProfile> userProfile = root.join("user");
                List<Predicate> predicates = new ArrayList<>();

                for (String field : searchFields) {
                    Path<String> path = userProfile.get(field);
                    Predicate predicate = cb.like(cb.lower(path), "%" + search.toLowerCase() + "%");
                    predicates.add(predicate);
                }

                return cb.or(predicates.toArray(new Predicate[0]));
            }

            return cb.conjunction();
        };
    }

}
