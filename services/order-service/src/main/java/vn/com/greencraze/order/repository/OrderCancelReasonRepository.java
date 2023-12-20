package vn.com.greencraze.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import vn.com.greencraze.order.entity.OrderCancelReason;

public interface OrderCancelReasonRepository extends JpaRepository<OrderCancelReason, Long>,
        JpaSpecificationExecutor<OrderCancelReason> {
}