package vn.com.greencraze.infrastructure.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import vn.com.greencraze.commons.api.ListResponse;
import vn.com.greencraze.commons.api.RestResponse;
import vn.com.greencraze.commons.auth.AuthFacade;
import vn.com.greencraze.commons.domain.dto.CreateNotificationRequest;
import vn.com.greencraze.commons.exception.ResourceNotFoundException;
import vn.com.greencraze.infrastructure.client.user.UserServiceClient;
import vn.com.greencraze.infrastructure.dto.response.notification.CreateNotificationResponse;
import vn.com.greencraze.infrastructure.dto.response.notification.GetCountNotificationResponse;
import vn.com.greencraze.infrastructure.dto.response.notification.GetListNotificationResponse;
import vn.com.greencraze.infrastructure.entity.Notification;
import vn.com.greencraze.infrastructure.exception.InvalidUserIdException;
import vn.com.greencraze.infrastructure.mapper.NotificationMapper;
import vn.com.greencraze.infrastructure.repository.NotificationRepository;
import vn.com.greencraze.infrastructure.repository.specification.NotificationSpecification;
import vn.com.greencraze.infrastructure.service.INotificationService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements INotificationService {

    private final SimpMessagingTemplate simpMessagingTemplate;

    private final NotificationRepository notificationRepository;

    private final NotificationMapper notificationMapper;

    private final UserServiceClient userServiceClient;

    private final AuthFacade authFacade;

    private static final String RESOURCE_NAME = "Notification";

    private static final List<String> SEARCH_FIELDS = List.of("title", "content");

    @Override
    public RestResponse<ListResponse<GetListNotificationResponse>> getListNotification(
            Integer page, Integer size, Boolean isSortAscending, String columnName, String search, Boolean all) {
        String userId = authFacade.getUserId();
        NotificationSpecification notificationSpecification = new NotificationSpecification();
        Specification<Notification> sortable = notificationSpecification.sortable(isSortAscending, columnName);
        Specification<Notification> searchable = notificationSpecification.searchable(SEARCH_FIELDS, search);
        Specification<Notification> filterableByUserId = notificationSpecification.filterable(userId);
        Pageable pageable = all ? Pageable.unpaged() : PageRequest.of(page - 1, size);
        Page<GetListNotificationResponse> responses = notificationRepository
                .findAll(sortable.and(searchable).and(filterableByUserId), pageable)
                .map(notificationMapper::notificationToGetListNotificationResponse);
        return RestResponse.ok(ListResponse.of(responses));
    }

    @Override
    public RestResponse<GetCountNotificationResponse> getCountNotification() {
        String userId = authFacade.getUserId();
        long count = notificationRepository.countByUserIdAndStatus(userId, false);
        return RestResponse.ok(GetCountNotificationResponse.builder()
                .count(count)
                .build());
    }

    @Override
    public void createNotification(CreateNotificationRequest request) {
        switch (request.type()) {
            case ORDER -> sendToUser(request);
            case SALE -> sendToAllUser(request);
        }
    }

    @Override
    public void updateNotification(Long id) {
        String userId = authFacade.getUserId();
        Notification notification = notificationRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "id", id));
        if (!notification.getStatus()) {
            notification.setStatus(true);
            notificationRepository.save(notification);
        }
    }

    @Override
    public void updateAllNotification() {
        String userId = authFacade.getUserId();
        List<Notification> notifications = notificationRepository.findAllByUserId(userId);
        for (Notification notification : notifications) {
            if (!notification.getStatus()) {
                notification.setStatus(true);
                notificationRepository.save(notification);
            }
        }
    }

    private void sendToUser(CreateNotificationRequest request) {
        if (request.userId() == null) {
            throw new InvalidUserIdException("Invalid user id");
        }
        notificationRepository.save(notificationMapper.createNotificationRequestToNotification(request));
        long count = notificationRepository.countByUserIdAndStatus(request.userId(), false);
        simpMessagingTemplate.convertAndSendToUser(request.userId(), "/notifications",
                CreateNotificationResponse.builder()
                        .title(request.title())
                        .content(request.content())
                        .count(count)
                        .build());
    }

    private void sendToAllUser(CreateNotificationRequest request) {
        List<String> userIds = userServiceClient.getAllUserId();
        for (String userId : userIds) {
            notificationRepository.save(notificationMapper
                    .createNotificationRequestToNotification(request.withUserId(userId)));

            long count = notificationRepository.countByUserIdAndStatus(userId, false);
            simpMessagingTemplate.convertAndSendToUser(userId, "/notifications",
                    CreateNotificationResponse.builder()
                            .title(request.title())
                            .content(request.content())
                            .count(count)
                            .build());
        }
    }

}
