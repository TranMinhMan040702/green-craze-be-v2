package vn.com.greencraze.infrastructure.service;

import org.springframework.web.multipart.MultipartFile;

public interface IUploadService {

    String uploadFile(MultipartFile file);

}
