package vn.com.greencraze.infrastructure.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import vn.com.greencraze.commons.api.RestResponse;
import vn.com.greencraze.infrastructure.service.ISystemService;

@RestController
@RequestMapping("/systems")
@RequiredArgsConstructor
public class SystemController {

    private final ISystemService systemService;

    @PostMapping
    public ResponseEntity<RestResponse<String>> uploadLogo(MultipartFile file) {
        return ResponseEntity.ok(systemService.uploadLogo(file));
    }

}
