package vn.com.greencraze.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import vn.com.greencraze.product.entity.Product;
import vn.com.greencraze.product.entity.Variant;

import java.util.List;

public interface VariantRepository extends JpaRepository<Variant, Long>, JpaSpecificationExecutor<Variant> {

    List<Variant> findAllByProduct(Product product);
    
}