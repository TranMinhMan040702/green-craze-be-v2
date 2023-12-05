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
import vn.com.greencraze.user.client.product.ProductServiceClient;
import vn.com.greencraze.user.client.product.dto.request.UpdateListProductReviewRequest;
import vn.com.greencraze.user.client.product.dto.request.UpdateOneProductReviewRequest;
import vn.com.greencraze.user.client.product.dto.response.GetOneProductResponse;
import vn.com.greencraze.user.dto.request.review.CreateReviewRequest;
import vn.com.greencraze.user.dto.request.review.GetOrderReviewRequest;
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
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements IReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;
    private final UserProfileRepository userProfileRepository;
    private final IUploadService uploadService;
    private final ProductServiceClient productServiceClient;
    private final AuthFacade authFacade;
    private static final String RESOURCE_NAME = "Review";
    private static final List<String> SEARCH_FIELDS = List.of("name", "title");

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
                .map(reviewMapper::reviewToGetListReviewResponse);

        for (GetListReviewResponse response : responses.getContent()) {
            RestResponse<GetOneProductResponse> productResponse = productServiceClient
                    .getOneProduct(response.productId());
            GetListReviewResponse.ProductResponse product = reviewMapper
                    .productResponseToGetListReviewProductResponse(productResponse.data());

            response = response.withProduct(product);
        }

        return RestResponse.ok(ListResponse.of(responses));
    }

    @Override
    public RestResponse<List<Long>> getCountReview(Long productId) {

        ReviewSpecification reviewSpecification = new ReviewSpecification();
        Specification<Review> filterable = reviewSpecification.filterable(productId, null, null);

        List<Review> reviews = reviewRepository
                .findAll(filterable);
        Long[] count = new Long[6];
        for (int i = 1; i <= 5; i++) {
            int finalI = i;
            long cnt = 0;
            if (!reviews.isEmpty()) {
                cnt = reviews.stream().filter(review -> review.getRating() == finalI).count();
            }
            count[i] = cnt;
        }
        count[0] = (long) reviews.size();

        return RestResponse.ok(Arrays.asList(count));
    }

    @Override
    public RestResponse<List<GetListReviewResponse>> getTop5ReviewLatest() {
        ReviewSpecification reviewSpecification = new ReviewSpecification();
        Specification<Review> sortable = reviewSpecification.sortable(false, "createdAt");

        Pageable pageable = PageRequest.of(1, 5);

        Page<GetListReviewResponse> responses = reviewRepository
                .findAll(sortable, pageable)
                .map(reviewMapper::reviewToGetListReviewResponse);

        List<GetListReviewResponse> updatedListReviewResponse = new ArrayList<>();

        for (GetListReviewResponse response : responses.getContent()) {
            RestResponse<GetOneProductResponse> productResponse = productServiceClient
                    .getOneProduct(response.productId());
            GetListReviewResponse.ProductResponse product = reviewMapper
                    .productResponseToGetListReviewProductResponse(productResponse.data());

            updatedListReviewResponse.add(response.withProduct(product));
        }

        return RestResponse.ok(updatedListReviewResponse);
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

        RestResponse<GetOneReviewResponse> response = reviewRepository.findByUserAndOrderItemId(user, orderItemId)
                .map(reviewMapper::reviewToGetOneReviewResponse)
                .map(RestResponse::ok)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "orderItemId", orderItemId));

        Long productId = response.data().productId();

        RestResponse<GetOneProductResponse> productResponse = productServiceClient.getOneProduct(productId);
        if (productResponse == null) {
            throw new ResourceNotFoundException(RESOURCE_NAME, "productId", productId);
        }

        GetOneReviewResponse.ProductResponse product = reviewMapper
                .productResponseToGetOneReviewProductResponse(productResponse.data());

        return RestResponse.ok(response.data().withProduct(product));
    }

    private void updateProductReview(Long productId) {
        List<Review> reviews = reviewRepository.findByProductIdAndStatus(productId, true)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "productId", productId));

        Double rating = reviews.stream().reduce(0.0,
                (a, b) -> a + b.getRating(), Double::sum) / reviews.size();

        productServiceClient.updateProductReview(productId, new UpdateOneProductReviewRequest(rating));
    }

    private void updateProductReview(List<Long> productIds) {
        UpdateListProductReviewRequest request = new UpdateListProductReviewRequest(new ArrayList<>());

        for (Long productId : productIds) {
            List<Review> reviews = reviewRepository.findByProductIdAndStatus(productId, true)
                    .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "productId", productId));

            Double rating = reviews.stream().reduce(0.0,
                    (a, b) -> a + b.getRating(), Double::sum) / reviews.size();

            request.productReviews().add(new UpdateListProductReviewRequest.UpdateOneProductReview(productId, rating));
        }

        productServiceClient.updateListProductReview(request);
    }

    @Override
    public RestResponse<CreateReviewResponse> createReview(CreateReviewRequest request) {
        String userId = authFacade.getUserId();
        UserProfile user = userProfileRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "userId", userId));

        Review review = reviewMapper.createReviewRequestToReview(request);
        if (request.image() != null)
            review.setImage(uploadService.uploadFile(request.image()));
        review.setUser(user);

        reviewRepository.save(review);
        // TODO: pub message to update product review
        updateProductReview(review.getProductId());

        return RestResponse.created(reviewMapper.reviewToCreateReviewResponse(review));
    }

    @Transactional(rollbackOn = {ResourceNotFoundException.class})
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
        // TODO: pub message to update product review
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
    public void deleteOneReview(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "id", id));

        review.setStatus(false);

        reviewRepository.save(review);
        updateProductReview(review.getProductId());
    }

    @Override
    public void toggleReview(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "id", id));

        review.setStatus(!review.getStatus());

        reviewRepository.save(review);
    }

    @Override
    public void deleteListReview(List<Long> ids) {
        List<Long> productIds = new ArrayList<>();

        for (Long id : ids) {
            Review review = reviewRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "id", id));

            review.setStatus(false);
            productIds.add(review.getProductId());

            reviewRepository.save(review);
        }

        updateProductReview(productIds);
    }

    @Override
    public RestResponse<GetOrderReviewResponse> getOrderReview(GetOrderReviewRequest request) {
        boolean isReview = true;
        Instant reviewedDate = null;
        List<Instant> reviewTimes = new ArrayList<>();

        for (Long id : request.orderItemIds()) {
            Review review = reviewRepository.findByOrderItemId(id);
            if (review == null) {
                isReview = false;
                break;
            }
            reviewTimes.add(review.getCreatedAt());
        }
        if (!reviewTimes.isEmpty() && reviewTimes.size() == request.orderItemIds().size())
            reviewedDate = reviewTimes.stream().max(Instant::compareTo).get();

        return RestResponse.ok(new GetOrderReviewResponse(isReview, reviewedDate));
    }

}
