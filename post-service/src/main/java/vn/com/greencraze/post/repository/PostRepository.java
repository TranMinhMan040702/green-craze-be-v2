package vn.com.greencraze.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import vn.com.greencraze.post.entity.Post;

public interface PostRepository extends JpaRepository<Post, Long>, JpaSpecificationExecutor<Post> {
}