package vn.com.greencraze.address.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import vn.com.greencraze.address.entity.District;
import vn.com.greencraze.address.entity.Province;

import java.util.List;
import java.util.Optional;

public interface DistrictRepository extends JpaRepository<District, Long>, JpaSpecificationExecutor<District> {

    Optional<List<District>> findAllByProvince(Province province);

}
