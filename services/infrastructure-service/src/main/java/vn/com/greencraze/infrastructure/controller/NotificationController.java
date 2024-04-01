package vn.com.greencraze.infrastructure.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import vn.com.greencraze.commons.api.ListResponse;
import vn.com.greencraze.commons.api.RestResponse;
import vn.com.greencraze.infrastructure.dto.response.notification.GetCountNotificationResponse;
import vn.com.greencraze.infrastructure.dto.response.notification.GetListNotificationResponse;
import vn.com.greencraze.infrastructure.service.INotificationService;

@RestController
@RequestMapping("/notifications")
@Tag(name = "notification :: Notification")
@RequiredArgsConstructor
public class NotificationController {

    private final INotificationService notificationService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get a list notifications")
    public ResponseEntity<RestResponse<ListResponse<GetListNotificationResponse>>> getListNotification(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "true") boolean isSortAscending,
            @RequestParam(defaultValue = "id") String columnName,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) boolean all
    ) {
        return ResponseEntity.ok(notificationService.getListNotification(
                page, size, isSortAscending, columnName, search, all));
    }

    @GetMapping(value = "/count", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get count notifications")
    public ResponseEntity<RestResponse<GetCountNotificationResponse>> getCountNotification() {
        return ResponseEntity.ok(notificationService.getCountNotification());
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Update status notification")
    public ResponseEntity<Void> updateNotification(@PathVariable Long id) {
        notificationService.updateNotification(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/all")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Update status all notification")
    public ResponseEntity<Void> updateAllNotification() {
        notificationService.updateAllNotification();
        return ResponseEntity.noContent().build();
    }

}
