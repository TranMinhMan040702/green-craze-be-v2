package vn.com.greencraze.product.repository.specification;

import org.springframework.data.jpa.domain.Specification;
import vn.com.greencraze.commons.specification.BaseSpecification;
import vn.com.greencraze.product.entity.ProductCategory;

public class ProductCategorySpecification extends BaseSpecification<ProductCategory> {

    public Specification<ProductCategory> filterable(Long parentCategoryId) {
        return (root, query, cb) ->
                (parentCategoryId != null)
                        ? cb.and(cb.equal(root.get("parentCategory").get("id"), parentCategoryId))
                        : null;
    }

}
