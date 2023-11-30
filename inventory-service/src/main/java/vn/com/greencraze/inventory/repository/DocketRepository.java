package vn.com.greencraze.inventory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import vn.com.greencraze.inventory.entity.Docket;

import java.util.Optional;

public interface DocketRepository extends JpaRepository<Docket, Long>, JpaSpecificationExecutor<Docket> {

    Optional<Docket> findByProductId(Long productId);
    
}