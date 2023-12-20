package vn.com.greencraze.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.com.greencraze.order.entity.OrderItem;
import vn.com.greencraze.order.entity.query.OrderItemQuery;

import java.time.Instant;
import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long>, JpaSpecificationExecutor<OrderItem> {

    @Query("""
            select new vn.com.greencraze.order.entity.query.OrderItemQuery(orderItem.variantId, sum(orderItem.quantity))
            from OrderItem orderItem
                join orderItem.order orders
                join orders.transaction transaction
            where orders.status = 'DELIVERED'
                and orders.paymentStatus = true
                and transaction.completedAt >= :startDate
                and transaction.completedAt <= :endDate
            group by orderItem.variantId
            """)
    List<OrderItemQuery> findAllByCreatedAt(
            @Param("startDate") Instant startDate, @Param("endDate") Instant endDate);

}