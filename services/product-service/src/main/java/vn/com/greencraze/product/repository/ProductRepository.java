package vn.com.greencraze.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.com.greencraze.product.entity.Product;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    @Query(value = """
            SELECT p.* FROM product p
            JOIN product_category c ON p.product_category_id = c.id
            WHERE c.status = true AND p.slug = :slug""",
            nativeQuery = true)
    Optional<Product> findBySlug(@Param("slug") String slug);

}