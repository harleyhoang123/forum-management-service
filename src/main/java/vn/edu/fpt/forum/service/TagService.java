package vn.edu.fpt.forum.service;

import vn.edu.fpt.forum.dto.common.PageableResponse;
import vn.edu.fpt.forum.dto.request.tag.CreateTagOwnerRequest;
import vn.edu.fpt.forum.dto.request.tag.GetTagOwnerRequest;
import vn.edu.fpt.forum.dto.request.tag.UpdateTagOwnerRequest;
import vn.edu.fpt.forum.dto.response.tag.CreateTagOwnerResponse;
import vn.edu.fpt.forum.dto.response.tag.GetTagOwnerResponse;

/**
 * @author : Hoang Lam
 * @product : Charity Management System
 * @project : Charity System
 * @created : 24/11/2022 - 04:26
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
public interface TagService {

    CreateTagOwnerResponse createTag(CreateTagOwnerRequest request);

    void updateTagOwner(String tagId, UpdateTagOwnerRequest request);

    PageableResponse<GetTagOwnerResponse> getTagOwnerByCondition(GetTagOwnerRequest request);

    void deleteTagOwner(String tagId);
}
