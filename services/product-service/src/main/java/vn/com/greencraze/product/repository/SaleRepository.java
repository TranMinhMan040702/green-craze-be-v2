package vn.com.greencraze.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import vn.com.greencraze.product.entity.Sale;
import vn.com.greencraze.product.enumeration.SaleStatus;

import java.util.Optional;

public interface SaleRepository extends JpaRepository<Sale, Long>, JpaSpecificationExecutor<Sale> {

    Boolean existsByStatus(SaleStatus status);

    Optional<Sale> findByStatus(SaleStatus status);

}