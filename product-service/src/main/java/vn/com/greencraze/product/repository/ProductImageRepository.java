package vn.com.greencraze.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import vn.com.greencraze.product.entity.Product;
import vn.com.greencraze.product.entity.ProductImage;

import java.util.List;
import java.util.Optional;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long>,
        JpaSpecificationExecutor<ProductImage> {

    List<ProductImage> findAllByProduct(Product product);

    Optional<ProductImage> findByProductAndIsDefault(Product product, Boolean isDefault);

}