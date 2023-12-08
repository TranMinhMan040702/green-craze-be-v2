package vn.com.greencraze.user.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.com.greencraze.commons.api.ListResponse;
import vn.com.greencraze.commons.api.RestResponse;
import vn.com.greencraze.commons.auth.AuthFacade;
import vn.com.greencraze.commons.exception.ResourceNotFoundException;
import vn.com.greencraze.user.client.order.OrderServiceClient;
import vn.com.greencraze.user.client.order.dto.response.GetOneOrderItemResponse;
import vn.com.greencraze.user.client.product.ProductServiceClient;
import vn.com.greencraze.user.client.product.dto.request.UpdateListProductReviewRequest;
import vn.com.greencraze.user.client.product.dto.request.UpdateOneProductReviewRequest;
import vn.com.greencraze.user.client.product.dto.response.GetOneProductResponse;
import vn.com.greencraze.user.client.product.dto.response.GetOneVariantResponse;
import vn.com.greencraze.user.dto.request.review.CreateReviewRequest;
import vn.com.greencraze.user.dto.request.review.ReplyReviewRequest;
import vn.com.greencraze.user.dto.request.review.UpdateReviewRequest;
import vn.com.greencraze.user.dto.response.review.CreateReviewResponse;
import vn.com.greencraze.user.dto.response.review.GetListReviewResponse;
import vn.com.greencraze.user.dto.response.review.GetOneReviewResponse;
import vn.com.greencraze.user.dto.response.review.GetOrderReviewResponse;
import vn.com.greencraze.user.entity.Review;
import vn.com.greencraze.user.entity.UserProfile;
import vn.com.greencraze.user.mapper.ReviewMapper;
import vn.com.greencraze.user.repository.ReviewRepository;
import vn.com.greencraze.user.repository.UserProfileRepository;
import vn.com.greencraze.user.repository.specification.ReviewSpecification;
import vn.com.greencraze.user.service.IReviewService;
import vn.com.greencraze.user.service.IUploadService;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements IReviewService {

    private final ReviewRepository reviewRepository;
    private final UserProfileRepository userProfileRepository;
    private final IUploadService uploadService;

    private final ReviewMapper reviewMapper;

    private final ProductServiceClient productServiceClient;
    private final OrderServiceClient orderServiceClient;

    private final AuthFacade authFacade;
    private static final String RESOURCE_NAME = "Review";
    private static final List<String> SEARCH_FIELDS = List.of("title");

    private GetListReviewResponse mapReviewToGetListReviewResponse(Review review) {
        GetListReviewResponse resp = reviewMapper.reviewToGetListReviewResponse(review);

        RestResponse<GetOneProductResponse> productResponse = productServiceClient
                .getOneProduct(review.getProductId());
        GetListReviewResponse.ProductResponse product = reviewMapper
                .productResponseToGetListReviewProductResponse(productResponse.data());

        RestResponse<GetOneOrderItemResponse> orderItemResponse = orderServiceClient
                .GetOneOrderItem(review.getOrderItemId());

        RestResponse<GetOneVariantResponse> variantResponse = productServiceClient
                .getOneVariant(orderItemResponse.data().variantId());

        return resp.withProduct(product)
                .withVariantName(variantResponse.data().name());
    }

    @Override
    public RestResponse<ListResponse<GetListReviewResponse>> getListReview(
            Integer page, Integer size, Boolean isSortAscending, String columnName, String search, Boolean all,
            Long productId, Long rating, Boolean status) {

        ReviewSpecification reviewSpecification = new ReviewSpecification();
        Specification<Review> sortable = reviewSpecification.sortable(isSortAscending, columnName);
        Specification<Review> searchable = reviewSpecification.searchable(SEARCH_FIELDS, search);
        Specification<Review> filterable = reviewSpecification.filterable(productId, rating, status);

        Pageable pageable = all ? Pageable.unpaged() : PageRequest.of(page - 1, size);

        Page<GetListReviewResponse> responses = reviewRepository
                .findAll(sortable.and(searchable).and(filterable), pageable)
                .map(this::mapReviewToGetListReviewResponse);

        return RestResponse.ok(ListResponse.of(responses));
    }

    @Override
    public RestResponse<List<Long>> getCountReview(Long productId) {

        ReviewSpecification reviewSpecification = new ReviewSpecification();
        Specification<Review> filterable = reviewSpecification.filterable(productId, null, true);

        List<Review> reviews = reviewRepository
                .findAll(filterable);
        Long[] count = new Long[6];
        for (int i = 5; i >= 1; i--) {
            int finalI = i;
            long cnt = 0;
            if (!reviews.isEmpty()) {
                cnt = reviews.stream().filter(review -> review.getRating() == finalI).count();
            }
            count[6 - i] = cnt;
        }
        count[0] = (long) reviews.size();

        return RestResponse.ok(Arrays.asList(count));
    }

    @Override
    public RestResponse<List<GetListReviewResponse>> getTop5ReviewLatest() {
        ReviewSpecification reviewSpecification = new ReviewSpecification();
        Specification<Review> sortable = reviewSpecification.sortable(false, "createdAt");

        Pageable pageable = PageRequest.of(0, 5);

        Page<GetListReviewResponse> responses = reviewRepository
                .findAll(sortable, pageable)
                .map(this::mapReviewToGetListReviewResponse);

        return RestResponse.ok(responses.getContent());
    }

    @Override
    public RestResponse<GetOneReviewResponse> getOneReview(Long id) {
        RestResponse<GetOneReviewResponse> response = reviewRepository.findById(id)
                .map(reviewMapper::reviewToGetOneReviewResponse)
                .map(RestResponse::ok)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "id", id));

        Long productId = response.data().productId();

        RestResponse<GetOneProductResponse> productResponse = productServiceClient.getOneProduct(productId);
        if (productResponse == null) {
            throw new ResourceNotFoundException(RESOURCE_NAME, "productId", productId);
        }

        return RestResponse.ok(response.data().withProduct(
                reviewMapper.productResponseToGetOneReviewProductResponse(productResponse.data()))
        );
    }

    @Override
    public RestResponse<GetOneReviewResponse> getOneReviewByOrderItem(Long orderItemId) {
        String userId = authFacade.getUserId();
        UserProfile user = userProfileRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "userId", userId));

        Optional<GetOneReviewResponse> response = reviewRepository.findByUserAndOrderItemId(user, orderItemId)
                .map(reviewMapper::reviewToGetOneReviewResponse);

        if (response.isEmpty()) {
            return RestResponse.ok(null);
        }

        Long productId = response.get().productId();

        RestResponse<GetOneProductResponse> productResponse = productServiceClient.getOneProduct(productId);
        if (productResponse == null) {
            throw new ResourceNotFoundException(RESOURCE_NAME, "productId", productId);
        }

        GetOneReviewResponse.ProductResponse product = reviewMapper
                .productResponseToGetOneReviewProductResponse(productResponse.data());

        return RestResponse.ok(response.get().withProduct(product).withUser(null));
    }

    private void updateProductReview(Long productId) {
        List<Review> reviews = reviewRepository.findByProductIdAndStatus(productId, true)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "productId", productId));

        double rating = 5.0;
        if (!reviews.isEmpty()) {
            rating = reviews.stream().reduce(0.0,
                    (a, b) -> a + b.getRating(), Double::sum) / reviews.size();
        }

        productServiceClient.updateProductReview(productId, new UpdateOneProductReviewRequest(rating));
    }


    private void updateProductReview(Set<Long> productIds) {
        UpdateListProductReviewRequest request = new UpdateListProductReviewRequest(new ArrayList<>());

        for (Long productId : productIds) {
            List<Review> reviews = reviewRepository.findByProductIdAndStatus(productId, true)
                    .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "productId", productId));

            double rating = 5.0;
            if (!reviews.isEmpty()) {
                rating = reviews.stream().reduce(0.0,
                        (a, b) -> a + b.getRating(), Double::sum) / reviews.size();
            }

            request.productReviews().add(new UpdateListProductReviewRequest.UpdateOneProductReview(productId, rating));
        }

        productServiceClient.updateListProductReview(request);
    }

    @Override
    @Transactional
    public RestResponse<CreateReviewResponse> createReview(CreateReviewRequest request) {
        String userId = authFacade.getUserId();
        UserProfile user = userProfileRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "userId", userId));

        Review review = reviewMapper.createReviewRequestToReview(request);
        if (request.image() != null)
            review.setImage(uploadService.uploadFile(request.image()));
        review.setUser(user);
        review.setStatus(true);

        reviewRepository.save(review);
        updateProductReview(review.getProductId());

        return RestResponse.created(reviewMapper.reviewToCreateReviewResponse(review));
    }

    @Transactional
    @Override
    public void updateReview(Long id, UpdateReviewRequest request) {
        Review review = reviewRepository.findById(id)
                .map(b -> reviewMapper.updateReviewRequestToReview(b, request))
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "id", id));

        if (request.image() != null) {
            review.setImage(uploadService.uploadFile(request.image()));
        } else if (request.isDeleteImage()) {
            review.setImage(null);
        }
        updateProductReview(review.getProductId());

        reviewRepository.save(review);
    }

    @Override
    public void replyReview(Long id, ReplyReviewRequest request) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "id", id));

        review.setReply(request.reply());

        reviewRepository.save(review);
    }

    @Override
    @Transactional
    public void deleteOneReview(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "id", id));

        review.setStatus(false);

        reviewRepository.save(review);
        updateProductReview(review.getProductId());
    }

    @Override
    @Transactional
    public void toggleReview(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "id", id));

        review.setStatus(!review.getStatus());

        reviewRepository.save(review);
        updateProductReview(review.getProductId());
    }

    @Override
    @Transactional
    public void deleteListReview(List<Long> ids) {
        Set<Long> productIds = new HashSet<>();

        for (Long id : ids) {
            Review review = reviewRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "id", id));

            review.setStatus(false);
            productIds.add(review.getProductId());

            reviewRepository.save(review);
        }

        updateProductReview(productIds);
    }

    // call from other services
    @Override
    public RestResponse<GetOrderReviewResponse> getOrderReview(List<Long> orderItemIds) {
        boolean isReview = true;
        Instant reviewedDate = null;
        List<Instant> reviewTimes = new ArrayList<>();

        for (Long id : orderItemIds) {
            Review review = reviewRepository.findByOrderItemId(id);
            if (review == null) {
                isReview = false;
                break;
            }
            reviewTimes.add(review.getUpdatedAt());
        }
        if (!reviewTimes.isEmpty() && reviewTimes.size() == orderItemIds.size())
            reviewedDate = reviewTimes.stream().max(Instant::compareTo).get();

        return RestResponse.ok(new GetOrderReviewResponse(isReview, reviewedDate));
    }

}
