package vn.edu.fpt.forum.service;

import vn.edu.fpt.forum.dto.request.image.AddImageRequest;
import vn.edu.fpt.forum.dto.response.image.AddImageResponse;

/**
 * @author : Hoang Lam
 * @product : Charity Management System
 * @project : Charity System
 * @created : 21/11/2022 - 14:38
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
public interface ImageService {

    AddImageResponse addImageToBucket(AddImageRequest request);
}
