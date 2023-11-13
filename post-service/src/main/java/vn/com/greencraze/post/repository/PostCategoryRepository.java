package vn.com.greencraze.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import vn.com.greencraze.post.entity.PostCategory;

public interface PostCategoryRepository extends JpaRepository<PostCategory, Long>,
        JpaSpecificationExecutor<PostCategory> {
}