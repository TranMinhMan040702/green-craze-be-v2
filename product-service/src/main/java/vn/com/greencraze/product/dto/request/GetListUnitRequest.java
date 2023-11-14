package vn.com.greencraze.product.dto.request;

public record GetListUnitRequest(
        String search,
        Boolean isSortAscending,
        String ColumnName,
        Integer pageIndex,
        Integer pageSize
) {}
