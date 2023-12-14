package vn.com.greencraze.infrastructure.service;

import org.springframework.web.multipart.MultipartFile;
import vn.com.greencraze.commons.api.RestResponse;

public interface ISystemService {

    RestResponse<String> uploadLogo(MultipartFile file);

}
