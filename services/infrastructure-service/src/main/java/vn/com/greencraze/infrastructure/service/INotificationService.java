package vn.com.greencraze.infrastructure.service;

import vn.com.greencraze.commons.api.ListResponse;
import vn.com.greencraze.commons.api.RestResponse;
import vn.com.greencraze.commons.domain.dto.CreateNotificationRequest;
import vn.com.greencraze.infrastructure.dto.response.GetCountNotificationResponse;
import vn.com.greencraze.infrastructure.dto.response.GetListNotificationResponse;

public interface INotificationService {

    RestResponse<ListResponse<GetListNotificationResponse>> getListNotification(
            Integer page, Integer size, Boolean isSortAscending, String columnName, String search, Boolean all);

    RestResponse<GetCountNotificationResponse> getCountNotification();

    void createNotification(CreateNotificationRequest request);

    void updateNotification(Long id);

    void updateAllNotification();

}
