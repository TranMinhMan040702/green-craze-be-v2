package vn.com.greencraze.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import vn.com.greencraze.user.entity.Staff;

public interface StaffRepository extends JpaRepository<Staff, Long>, JpaSpecificationExecutor<Staff> {
}