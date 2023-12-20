package vn.com.greencraze.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import vn.com.greencraze.auth.entity.IdentityToken;

public interface IdentityTokenRepository extends JpaRepository<IdentityToken, String>,
        JpaSpecificationExecutor<IdentityToken> {
}