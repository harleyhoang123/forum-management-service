package vn.edu.fpt.forum.controller.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.fpt.forum.controller.ImageController;
import vn.edu.fpt.forum.dto.common.GeneralResponse;
import vn.edu.fpt.forum.dto.request.image.AddImageRequest;
import vn.edu.fpt.forum.dto.response.image.AddImageResponse;
import vn.edu.fpt.forum.service.ImageService;

/**
 * @author : Hoang Lam
 * @product : Charity Management System
 * @project : Charity System
 * @created : 21/11/2022 - 14:37
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
@RestController
@RequiredArgsConstructor
@Slf4j
public class ImageControllerImpl implements ImageController {

    private final ImageService imageService;

    @Override
    public ResponseEntity<GeneralResponse<AddImageResponse>> addImage(AddImageRequest request) {
        imageService.addImageToBucket(request);
        return null;
    }
}
