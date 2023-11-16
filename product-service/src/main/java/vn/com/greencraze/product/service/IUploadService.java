package vn.com.greencraze.product.service;

import org.springframework.web.multipart.MultipartFile;

public interface IUploadService {

    String uploadFile(MultipartFile file);

}
