package vn.com.greencraze.auth.repository.view;

import org.springframework.stereotype.Repository;
import vn.com.greencraze.auth.entity.Identity;
import vn.com.greencraze.auth.entity.view.UserProfileView;
import vn.com.greencraze.commons.repository.ReadOnlyRepository;

import java.util.Optional;

@Repository
public interface UserProfileViewRepository extends ReadOnlyRepository<UserProfileView, String> {

    Optional<UserProfileView> findByIdentity(Identity identity);

}
