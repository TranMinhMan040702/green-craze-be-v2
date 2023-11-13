package vn.com.greencraze.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import vn.com.greencraze.user.entity.UserFollowProduct;

public interface UserFollowProductRepository extends JpaRepository<UserFollowProduct, Long>,
        JpaSpecificationExecutor<UserFollowProduct> {
}