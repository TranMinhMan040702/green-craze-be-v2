package vn.com.greencraze.user.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import vn.com.greencraze.user.entity.UserFollowProduct;
import vn.com.greencraze.user.entity.UserProfile;

import java.util.Optional;

public interface UserFollowProductRepository extends JpaRepository<UserFollowProduct, Long>,
        JpaSpecificationExecutor<UserFollowProduct> {
    Page<UserFollowProduct> findAllByUser(UserProfile user, Pageable pageable);

    Optional<UserFollowProduct> findByUserAndProductId(UserProfile user, Long productId);
}