package vn.com.greencraze.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import vn.com.greencraze.user.entity.Review;
import vn.com.greencraze.user.entity.UserProfile;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long>, JpaSpecificationExecutor<Review> {

    Optional<Review> findByUserAndOrderItemId(UserProfile user, Long orderItemId);

    Optional<List<Review>> findByProductIdAndStatus(Long productId, boolean status);

    Review findByOrderItemId(Long orderItemId);

    Long countByRatingAndCreatedAtBetween(Integer rating, Instant startDate, Instant endDate);

}