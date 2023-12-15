package vn.com.greencraze.infrastructure.mapper;

import org.mapstruct.Mapper;
import vn.com.greencraze.infrastructure.dto.request.CreateNotificationRequest;
import vn.com.greencraze.infrastructure.dto.response.GetListNotificationResponse;
import vn.com.greencraze.infrastructure.entity.Notification;

@Mapper
public interface NotificationMapper {

    GetListNotificationResponse notificationToGetListNotificationResponse(Notification notification);

    Notification createNotificationRequestToNotification(CreateNotificationRequest createNotificationRequest);

}