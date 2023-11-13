package vn.com.greencraze.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import vn.com.greencraze.auth.entity.Identity;

public interface IdentityRepository extends JpaRepository<Identity, String>, JpaSpecificationExecutor<Identity> {
}