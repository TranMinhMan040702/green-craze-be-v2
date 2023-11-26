package vn.com.greencraze.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import vn.com.greencraze.user.entity.Cart;
import vn.com.greencraze.user.entity.CartItem;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long>, JpaSpecificationExecutor<CartItem> {

    Optional<List<CartItem>> findByCart(Cart cart);

    CartItem findByCartAndVariantId(Cart cart, Long variantId);

}