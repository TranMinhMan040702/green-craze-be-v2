package vn.com.greencraze.order.entity.query;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class OrderItemQuery {
    private Long variantId;
    private Long totalQuantity;
}
