package vn.com.greencraze.address.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import vn.com.greencraze.address.entity.Address;

import java.util.List;
import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, Long>, JpaSpecificationExecutor<Address> {

    List<Address> findAllByUserId(String userId);

    Optional<Address> findByIdAndUserId(Long id, String userId);

    Optional<Address> findByIdAndUserIdAndIsDefault(Long id, String userId, Boolean isDefault);

    Optional<Address> findByUserIdAndIsDefault(String userId, Boolean isDefault);

}
