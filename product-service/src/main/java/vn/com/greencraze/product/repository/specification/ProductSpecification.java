package vn.com.greencraze.product.repository.specification;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import vn.com.greencraze.commons.specification.BaseSpecification;
import vn.com.greencraze.product.entity.Brand;
import vn.com.greencraze.product.entity.Product;
import vn.com.greencraze.product.entity.ProductCategory;
import vn.com.greencraze.product.entity.Variant;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ProductSpecification extends BaseSpecification<Product> {

    public Specification<Product> filterable(
            String categorySlug, BigDecimal minPrice, BigDecimal maxPrice, Long brandId
    ) {
        List<Predicate> wheres = new ArrayList<>();
        return (root, query, cb) -> {
            if (categorySlug != null) {
                Join<Product, ProductCategory> category = root.join("productCategory");
                Predicate isEqualCategorySlug = cb.equal(category.get("slug"), categorySlug);
                wheres.add(isEqualCategorySlug);
            }
            if (minPrice != null && maxPrice != null) {
                Join<Product, Variant> variant = root.join("variants", JoinType.LEFT);
                Predicate priceBetween = cb.between(variant.get("itemPrice"), minPrice, maxPrice);
                wheres.add(priceBetween);
            }
            if (brandId != null) {
                Join<Product, Brand> brand = root.join("brand");
                Predicate isEqualBrandId = cb.equal(brand.get("id"), brandId);
                wheres.add(isEqualBrandId);
            }

            return cb.and(wheres.toArray(new Predicate[0]));
        };
    }

}
