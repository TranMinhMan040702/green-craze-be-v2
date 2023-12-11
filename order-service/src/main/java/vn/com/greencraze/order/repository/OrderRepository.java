package vn.com.greencraze.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import vn.com.greencraze.order.entity.Order;
import vn.com.greencraze.order.enumeration.OrderStatus;

import java.time.Instant;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {

    Optional<Order> findByUserIdAndCode(String userId, String code);

    Optional<Order> findByIdAndUserId(Long id, String userId);

    Long countByStatus(OrderStatus status);

    Long countByStatusAndCreatedAtBetween(OrderStatus status, Instant startDate, Instant endDate);

}