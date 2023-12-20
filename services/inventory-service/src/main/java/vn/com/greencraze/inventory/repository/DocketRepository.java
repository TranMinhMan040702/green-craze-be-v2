package vn.com.greencraze.inventory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import vn.com.greencraze.inventory.entity.Docket;
import vn.com.greencraze.inventory.enumeration.DocketType;

import java.time.Instant;
import java.util.List;

public interface DocketRepository extends JpaRepository<Docket, Long>, JpaSpecificationExecutor<Docket> {

    List<Docket> findByProductId(Long productId);

    List<Docket> findAllByType(DocketType type);

    List<Docket> findAllByTypeAndCreatedAtBetween(DocketType type, Instant startDate, Instant endDate);

}