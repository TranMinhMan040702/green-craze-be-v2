package vn.com.greencraze.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import vn.com.greencraze.order.entity.PaymentMethod;

public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Long>,
        JpaSpecificationExecutor<PaymentMethod> {
}