package vn.com.greencraze.product.repository.specification;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import vn.com.greencraze.commons.specification.BaseSpecification;
import vn.com.greencraze.product.dto.request.product.FilterProductRequest;
import vn.com.greencraze.product.entity.Brand;
import vn.com.greencraze.product.entity.Product;
import vn.com.greencraze.product.entity.ProductCategory;
import vn.com.greencraze.product.entity.Variant;
import vn.com.greencraze.product.enumeration.ProductStatus;

import java.util.ArrayList;
import java.util.List;

public class ProductSpecification extends BaseSpecification<Product> {

    public Specification<Product> filterable(String categorySlug) {
        List<Predicate> wheres = new ArrayList<>();
        return (root, query, cb) -> {
            if (categorySlug != null) {
                Join<Product, ProductCategory> category = root.join("productCategory");
                Predicate isEqualCategorySlug = cb.equal(category.get("slug"), categorySlug);
                wheres.add(isEqualCategorySlug);
            }
            return cb.and(wheres.toArray(new Predicate[0]));
        };
    }

    public Specification<Product> filterable(Boolean status) {
        List<Predicate> wheres = new ArrayList<>();
        return (root, query, cb) -> {
            if (status) {
                Predicate isNotEqualInactiveProductStatus = cb.notEqual(
                        root.get("status"), ProductStatus.INACTIVE);
                wheres.add(isNotEqualInactiveProductStatus);
            }
            return cb.and(wheres.toArray(new Predicate[0]));
        };
    }

    public Specification<Product> filterable(FilterProductRequest filter) {
        List<Predicate> wheres = new ArrayList<>();
        return (root, query, cb) -> {
            if (filter != null) {
                if (filter.categorySlug() != null) {
                    Join<Product, ProductCategory> category = root.join("productCategory");
                    Predicate isEqualCategorySlug = cb.equal(category.get("slug"), filter.categorySlug());
                    wheres.add(isEqualCategorySlug);
                }
                if (filter.minPrice() != null && filter.maxPrice() != null) {
                    Join<Product, Variant> variant = root.join("variants", JoinType.LEFT);
                    Predicate priceBetween = cb.between(variant.get("itemPrice"), filter.minPrice(), filter.maxPrice());
                    wheres.add(priceBetween);
                }
                if (filter.brandIds() != null && !filter.brandIds().isEmpty()) {
                    Join<Product, Brand> brand = root.join("brand");
                    for (Long id : filter.brandIds()) {
                        Predicate isEqualBrandId = cb.equal(brand.get("id"), id);
                        wheres.add(isEqualBrandId);
                    }
                }
                if (filter.categoryIds() != null && !filter.categoryIds().isEmpty()) {
                    Join<Product, ProductCategory> category = root.join("productCategory");
                    for (Long id : filter.categoryIds()) {
                        Predicate isEqualBrandId = cb.equal(category.get("id"), id);
                        wheres.add(isEqualBrandId);
                    }
                }
                if (filter.rating() != null) {
                    Predicate isGreaterThanOrEqualRating = cb.greaterThanOrEqualTo(root.get("rating"), filter.rating());
                    wheres.add(isGreaterThanOrEqualRating);
                }
            }
            return cb.and(wheres.toArray(new Predicate[0]));
        };
    }

}
