package vn.com.greencraze.address.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import vn.com.greencraze.address.entity.District;
import vn.com.greencraze.address.entity.Ward;

import java.util.List;
import java.util.Optional;

public interface WardRepository extends JpaRepository<Ward, Long>, JpaSpecificationExecutor<Ward> {

    Optional<List<Ward>> findAllByDistrict(District district);

}
