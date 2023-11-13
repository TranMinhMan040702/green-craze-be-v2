package vn.com.greencraze.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import vn.com.greencraze.post.entity.PostImage;

public interface PostImageRepository extends JpaRepository<PostImage, Long>, JpaSpecificationExecutor<PostImage> {
}