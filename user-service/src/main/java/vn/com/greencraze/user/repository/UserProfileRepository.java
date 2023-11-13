package vn.com.greencraze.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import vn.com.greencraze.user.entity.UserProfile;

public interface UserProfileRepository extends JpaRepository<UserProfile, String>,
        JpaSpecificationExecutor<UserProfile> {
}