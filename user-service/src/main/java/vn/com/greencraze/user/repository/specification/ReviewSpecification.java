package vn.com.greencraze.user.repository.specification;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import vn.com.greencraze.commons.specification.BaseSpecification;
import vn.com.greencraze.user.entity.Review;

import java.util.ArrayList;
import java.util.List;

public class ReviewSpecification extends BaseSpecification<Review> {
    public Specification<Review> filterable(Long productId, Long rating, Boolean status) {
        List<Predicate> wheres = new ArrayList<>();
        return (root, query, cb) -> {
            if (productId != null) {
                Predicate isEqualProductId = cb.equal(root.get("productId"), productId);
                wheres.add(isEqualProductId);
            }

            if (rating != null) {
                Predicate isEqualRating = cb.equal(root.get("rating"), rating);
                wheres.add(isEqualRating);
            }

            if (status) {
                Predicate isEqualStatus = cb.equal(root.get("status"), status);
                wheres.add(isEqualStatus);
            }

            return cb.and(wheres.toArray(new Predicate[0]));
        };
    }
}
