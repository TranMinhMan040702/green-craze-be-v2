package vn.com.greencraze.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import vn.com.greencraze.commons.annotation.InternalApi;
import vn.com.greencraze.commons.api.ListResponse;
import vn.com.greencraze.commons.api.RestResponse;
import vn.com.greencraze.commons.enumeration.Microservice;
import vn.com.greencraze.user.dto.request.review.CreateReviewRequest;
import vn.com.greencraze.user.dto.request.review.ReplyReviewRequest;
import vn.com.greencraze.user.dto.request.review.UpdateReviewRequest;
import vn.com.greencraze.user.dto.response.review.CreateReviewResponse;
import vn.com.greencraze.user.dto.response.review.GetListReviewResponse;
import vn.com.greencraze.user.dto.response.review.GetOneReviewResponse;
import vn.com.greencraze.user.dto.response.review.GetOrderReviewResponse;
import vn.com.greencraze.user.dto.response.review.GetTop5ReviewLatest;
import vn.com.greencraze.user.service.IReviewService;

import java.net.URI;
import java.time.Instant;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/reviews")
@Tag(name = "review :: reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final IReviewService reviewService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get a list of reviews")
    public ResponseEntity<RestResponse<ListResponse<GetListReviewResponse>>> getListReview(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "true") boolean isSortAscending,
            @RequestParam(defaultValue = "id") String columnName,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) boolean all,
            @RequestParam(required = false) Long productId,
            @RequestParam(required = false) Long rating,
            @RequestParam(required = false) boolean status
    ) {
        return ResponseEntity.ok(reviewService.getListReview(page, size, isSortAscending,
                columnName, search, all, productId, rating, status));
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get a review")
    public ResponseEntity<RestResponse<GetOneReviewResponse>> getOneReview(@PathVariable Long id) {
        return ResponseEntity.ok(reviewService.getOneReview(id));
    }

    @InternalApi(Microservice.META)
    @GetMapping(value = "/top5-review-latest", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get top 5 latest reviews")
    public ResponseEntity<List<GetTop5ReviewLatest>> getTop5ReviewLatest() {
        return ResponseEntity.ok(reviewService.getTop5ReviewLatest());
    }

    @GetMapping(value = "/count/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get list of count review")
    public ResponseEntity<RestResponse<List<Long>>> getCountReview(@PathVariable Long id) {
        return ResponseEntity.ok(reviewService.getCountReview(id));
    }

    @GetMapping(value = "/order-item/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get a review by order item")
    public ResponseEntity<RestResponse<GetOneReviewResponse>> getReviewByOrderItem(@PathVariable Long id) {
        return ResponseEntity.ok(reviewService.getOneReviewByOrderItem(id));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a review")
    public ResponseEntity<RestResponse<CreateReviewResponse>> createReview(
            @Valid CreateReviewRequest request
    ) {
        RestResponse<CreateReviewResponse> response = reviewService.createReview(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(response.data().id()).toUri();
        return ResponseEntity.created(location).body(response);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Update a review")
    public ResponseEntity<Void> updateReview(
            @PathVariable Long id, @Valid UpdateReviewRequest request
    ) {
        reviewService.updateReview(id, request);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping(value = "/toggle/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Toggle a review")
    public ResponseEntity<Void> toggleReview(
            @PathVariable Long id
    ) {
        reviewService.toggleReview(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping(value = "/reply/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Reply a review")
    public ResponseEntity<Void> replyReview(
            @PathVariable Long id, @RequestBody @Valid ReplyReviewRequest request
    ) {
        reviewService.replyReview(id, request);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a review")
    public ResponseEntity<Void> deleteOneReview(@PathVariable Long id) {
        reviewService.deleteOneReview(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a list of reviews")
    public ResponseEntity<Void> deleteListReview(@RequestParam List<Long> ids) {
        reviewService.deleteListReview(ids);
        return ResponseEntity.noContent().build();
    }

    // call from other services
    @GetMapping(value = "/internal/order-review")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get order review")
    public ResponseEntity<RestResponse<GetOrderReviewResponse>> getOrderReview(
            @RequestParam List<Long> orderItemIds
    ) {
        return ResponseEntity.ok(reviewService.getOrderReview(orderItemIds));
    }

    @InternalApi(Microservice.META)
    @GetMapping(value = "/rating-created-at")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get review by created at and rating")
    public ResponseEntity<Map<String, Long>> getReviewByRatingAndCreatedAt(
            @RequestParam Instant startDate, @RequestParam Instant endDate
    ) {
        return ResponseEntity.ok(reviewService.getReviewByRatingAndCreatedAt(startDate, endDate));
    }

}
