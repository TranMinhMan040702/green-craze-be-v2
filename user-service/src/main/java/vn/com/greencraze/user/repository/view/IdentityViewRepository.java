package vn.com.greencraze.user.repository.view;

import org.springframework.stereotype.Repository;
import vn.com.greencraze.commons.repository.ReadOnlyRepository;
import vn.com.greencraze.user.entity.view.IdentityView;

@Repository
public interface IdentityViewRepository extends ReadOnlyRepository<IdentityView, String> {}
