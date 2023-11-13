package vn.com.greencraze.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import vn.com.greencraze.product.entity.ProductImage;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long>,
        JpaSpecificationExecutor<ProductImage> {
}