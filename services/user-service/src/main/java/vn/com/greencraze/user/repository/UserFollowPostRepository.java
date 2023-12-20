package vn.com.greencraze.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import vn.com.greencraze.user.entity.UserFollowPost;

public interface UserFollowPostRepository extends JpaRepository<UserFollowPost, Long>,
        JpaSpecificationExecutor<UserFollowPost> {}