package vn.com.greencraze.commons.api;

import org.springframework.data.domain.Page;

import java.util.List;

public record ListResponse<T>(
        int currentItemCount,
        int itemsPerPage,
        long totalItems,
        int pageIndex,
        int totalPages,
        List<T> items
) {

    public static <T> ListResponse<T> of(Page<T> page) {
        return new ListResponse<>(
                page.getNumberOfElements(),
                page.getSize(),
                page.getTotalElements(),
                page.getNumber() + 1,
                page.getTotalPages(),
                page.getContent()
        );
    }

}
