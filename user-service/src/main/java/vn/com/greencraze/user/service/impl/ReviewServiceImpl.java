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
import vn.com.greencraze.user.client.product.dto.response.GetOneProductResponse;
import vn.com.greencraze.user.dto.request.review.CreateReviewRequest;
import vn.com.greencraze.user.dto.request.review.ReplyReviewRequest;
import vn.com.greencraze.user.dto.request.review.UpdateReviewRequest;
import vn.com.greencraze.user.dto.response.review.CreateReviewResponse;
import vn.com.greencraze.user.dto.response.review.GetListReviewResponse;
import vn.com.greencraze.user.dto.response.review.GetOneReviewResponse;
import vn.com.greencraze.user.entity.Review;
import vn.com.greencraze.user.entity.UserProfile;
import vn.com.greencraze.user.mapper.ReviewMapper;
import vn.com.greencraze.user.repository.ReviewRepository;
import vn.com.greencraze.user.repository.UserProfileRepository;
import vn.com.greencraze.user.repository.specification.ReviewSpecification;
import vn.com.greencraze.user.service.IReviewService;
import vn.com.greencraze.user.service.IUploadService;

import java.util.ArrayList;
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
            GetOneProductResponse productResponse = productServiceClient.getOneProduct(response.productId());
            GetListReviewResponse.ProductResponse product = reviewMapper.productResponseToGetListReviewProductResponse(productResponse);
            response.setProduct(product);
        }

        return RestResponse.ok(ListResponse.of(responses));
    }

    @Override
    public RestResponse<List<Long>> getCountReview(Long productId) {

        ReviewSpecification reviewSpecification = new ReviewSpecification();
        Specification<Review> filterable = reviewSpecification.filterable(productId, null, null);

        List<Review> reviews = reviewRepository
                .findAll(filterable);
        List<Long> count = new ArrayList<>(6);
        for (int i = 1; i <= 5; i++) {
            int finalI = i;
            count.set(i, reviews.stream().filter(review -> review.getRating() == finalI).count());
        }
        count.set(0, (long) reviews.size());

        return RestResponse.ok(count);
    }

    @Override
    public RestResponse<List<GetListReviewResponse>> getTop5ReviewLatest() {
        ReviewSpecification reviewSpecification = new ReviewSpecification();
        Specification<Review> sortable = reviewSpecification.sortable(false, "createdAt");

        Pageable pageable = PageRequest.of(1, 5);

        Page<GetListReviewResponse> responses = reviewRepository
                .findAll(sortable, pageable)
                .map(reviewMapper::reviewToGetListReviewResponse);

        for (GetListReviewResponse response : responses.getContent()) {
            GetOneProductResponse productResponse = productServiceClient.getOneProduct(response.productId());
            GetListReviewResponse.ProductResponse product = reviewMapper.productResponseToGetListReviewProductResponse(productResponse);
            response.setProduct(product);
        }

        return RestResponse.ok(responses.getContent());
    }

    @Override
    public RestResponse<GetOneReviewResponse> getOneReview(Long id) {
        RestResponse<GetOneReviewResponse> response = reviewRepository.findById(id)
                .map(reviewMapper::reviewToGetOneReviewResponse)
                .map(RestResponse::ok)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "id", id));

        Long productId = response.data().productId();

        GetOneProductResponse productResponse = productServiceClient.getOneProduct(productId);

        response.data().setProduct(reviewMapper.productResponseToGetOneReviewProductResponse(productResponse));

        return response;
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

        GetOneProductResponse productResponse = productServiceClient.getOneProduct(productId);

        response.data().setProduct(reviewMapper.productResponseToGetOneReviewProductResponse(productResponse));

        return response;
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
        // pub message to update product review

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
        // pub message to update product review

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
        for (Long id : ids) {
            Review review = reviewRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "id", id));

            review.setStatus(false);

            reviewRepository.save(review);
        }
    }

}
