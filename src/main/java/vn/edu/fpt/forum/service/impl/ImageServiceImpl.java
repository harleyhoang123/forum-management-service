package vn.edu.fpt.forum.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.edu.fpt.forum.dto.request.image.AddImageRequest;
import vn.edu.fpt.forum.dto.response.image.AddImageResponse;
import vn.edu.fpt.forum.service.ImageService;
import vn.edu.fpt.forum.service.S3BucketStorageService;


/**
 * @author : Hoang Lam
 * @product : Charity Management System
 * @project : Charity System
 * @created : 21/11/2022 - 14:38
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
@Service
@RequiredArgsConstructor
@Slf4j
public class ImageServiceImpl implements ImageService {

    private final S3BucketStorageService s3BucketStorageService;

    @Override
    public AddImageResponse addImageToBucket(AddImageRequest request) {
        String fileKey = s3BucketStorageService.uploadFile(request.getImage());
        return AddImageResponse.builder()
                .imageUrl(fileKey)
                .build();
    }
}
