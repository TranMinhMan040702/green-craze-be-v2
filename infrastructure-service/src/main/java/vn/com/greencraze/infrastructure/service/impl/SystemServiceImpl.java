package vn.com.greencraze.infrastructure.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.com.greencraze.commons.api.RestResponse;
import vn.com.greencraze.infrastructure.service.ISystemService;
import vn.com.greencraze.infrastructure.service.IUploadService;

@Service
@RequiredArgsConstructor
public class SystemServiceImpl implements ISystemService {

    private final IUploadService uploadService;

    @Override
    public RestResponse<String> uploadLogo(MultipartFile file) {
        return RestResponse.ok(uploadService.uploadFile(file));
    }
    
}
