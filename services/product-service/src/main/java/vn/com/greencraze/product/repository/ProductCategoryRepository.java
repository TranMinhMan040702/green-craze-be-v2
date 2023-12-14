package vn.com.greencraze.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import vn.com.greencraze.product.entity.ProductCategory;

import java.util.Optional;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long>,
        JpaSpecificationExecutor<ProductCategory> {

    Optional<ProductCategory> findProductCategoryBySlug(String slug);
    
}