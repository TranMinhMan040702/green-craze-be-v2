package vn.com.greencraze.product.repository.specification;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.com.greencraze.commons.specification.BaseSpecification;
import vn.com.greencraze.product.dto.request.product.FilterProductRequest;
import vn.com.greencraze.product.entity.Brand;
import vn.com.greencraze.product.entity.Product;
import vn.com.greencraze.product.entity.ProductCategory;
import vn.com.greencraze.product.entity.Variant;
import vn.com.greencraze.product.enumeration.ProductStatus;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductSpecification extends BaseSpecification<Product> {

    @Override
    public Specification<Product> sortable(Boolean isSortAscending, String columnName) {
        return (root, query, cb) -> {
            if (isSortAscending != null && columnName != null) {
                Order order;
                if (columnName.equals("name")) {
                    order = isSortAscending ? cb.asc(root.get("name")) : cb.desc(root.get("name"));
                } else {
                    return query.getRestriction();
                }
                query.orderBy(order);
            }
            return cb.conjunction();
        };
    }

    public Specification<Product> isActiveCategory(Boolean isActive) {
        return (root, query, cb) -> {
            if (isActive != null) {
                Join<Product, ProductCategory> category = root.join("productCategory");
                return cb.equal(category.get("status"), true);
            }
            return cb.conjunction();
        };
    }

    public Specification<Product> filterable(String categorySlug) {
        return (root, query, cb) -> {
            Predicate predicate = cb.conjunction();
            if (categorySlug != null) {
                Join<Product, ProductCategory> category = root.join("productCategory");
                predicate = cb.and(
                        predicate,
                        cb.equal(category.get("slug"), categorySlug),
                        cb.equal(category.get("status"), true)
                );
            }
            return predicate;
        };
    }

    public Specification<Product> filterable(Boolean status) {
        return (root, query, cb) ->
                (status)
                        ? cb.and(cb.notEqual(root.get("status"), ProductStatus.INACTIVE))
                        : cb.conjunction();
    }

    public Specification<Product> filterable(FilterProductRequest filter) {
        return (root, query, cb) -> {
            List<Predicate> wheres = new ArrayList<>();

            if (filter != null) {
                // Join for product category
                Join<Product, ProductCategory> categoryJoin = root.join("productCategory", JoinType.LEFT);
                if (filter.categoryIds() != null && !filter.categoryIds().isEmpty()) {
                    List<Predicate> categoryPredicates = new ArrayList<>();
                    for (Long id : filter.categoryIds()) {
                        categoryPredicates.add(cb.equal(categoryJoin.get("id"), id));
                    }
                    wheres.add(cb.or(categoryPredicates.toArray(new Predicate[0])));
                } else if (filter.categorySlug() != null) {
                    wheres.add(cb.equal(categoryJoin.get("slug"), filter.categorySlug()));
                }

                // Handle price filter with subquery
                if (filter.minPrice() != null && filter.maxPrice() != null) {
                    // Subquery to get the max item price for each product
                    Subquery<BigDecimal> subquery = query.subquery(BigDecimal.class);
                    Root<Variant> variantRoot = subquery.from(Variant.class);
                    subquery.select(cb.min(variantRoot.get("itemPrice")))
                            .where(cb.equal(variantRoot.get("product"), root));

                    // Join with Variant for the main query
                    Join<Product, Variant> variantJoin = root.join("variants", JoinType.LEFT);
                    Predicate priceBetween = cb.between(variantJoin.get("itemPrice"), filter.minPrice(), filter.maxPrice());
                    Predicate maxPricePredicate = cb.equal(variantJoin.get("itemPrice"), subquery);

                    if (filter.columnName().equals("price")) {
                        query.orderBy(filter.isSortAscending() ? cb.asc(variantJoin.get("itemPrice")) :
                                cb.desc(variantJoin.get("itemPrice")));
                    }

                    wheres.add(cb.and(maxPricePredicate, priceBetween));
                }

                // Join for brand
                if (filter.brandIds() != null && !filter.brandIds().isEmpty()) {
                    Join<Product, Brand> brandJoin = root.join("brand", JoinType.LEFT);
                    List<Predicate> brandPredicates = new ArrayList<>();
                    for (Long id : filter.brandIds()) {
                        brandPredicates.add(cb.equal(brandJoin.get("id"), id));
                    }
                    wheres.add(cb.or(brandPredicates.toArray(new Predicate[0])));
                }

                // Handle rating filter
                if (filter.rating() != null) {
                    wheres.add(cb.greaterThanOrEqualTo(root.get("rating"), filter.rating()));
                }
            }

            return cb.and(wheres.toArray(new Predicate[0]));
        };
    }

}
