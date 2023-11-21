package vn.com.greencraze.user.repository.specification;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import vn.com.greencraze.commons.specification.BaseSpecification;
import vn.com.greencraze.user.entity.Cart;
import vn.com.greencraze.user.entity.CartItem;

import java.util.ArrayList;
import java.util.List;

public class CartItemSpecification extends BaseSpecification<CartItem> {
    public Specification<CartItem> filterable(Cart cart) {
        List<Predicate> wheres = new ArrayList<>();
        return (root, query, cb) -> {
            if (cart != null) {
                Predicate isEqualCart = cb.equal(root.get("cart"), cart);
                wheres.add(isEqualCart);
            }
            return cb.and(wheres.toArray(new Predicate[0]));
        };
    }
}
