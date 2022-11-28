package vn.edu.fpt.forum.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import vn.edu.fpt.forum.dto.common.GeneralResponse;
import vn.edu.fpt.forum.dto.common.PageableResponse;
import vn.edu.fpt.forum.dto.request.tag.CreateTagOwnerRequest;
import vn.edu.fpt.forum.dto.request.tag.UpdateTagOwnerRequest;
import vn.edu.fpt.forum.dto.response.tag.CreateTagOwnerResponse;
import vn.edu.fpt.forum.dto.response.tag.GetTagOwnerResponse;

/**
 * @author : Hoang Lam
 * @product : Charity Management System
 * @project : Charity System
 * @created : 09/11/2022 - 09:28
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
@RequestMapping("${app.application-context}/public/api/v1/tags")
public interface TagController {

    ResponseEntity<GeneralResponse<CreateTagOwnerResponse>> createTagOwner(@RequestBody CreateTagOwnerRequest request);

    ResponseEntity<GeneralResponse<Object>> updateTagOwner(@PathVariable(name = "tag-id") String tagId, @RequestBody UpdateTagOwnerRequest request);

    ResponseEntity<GeneralResponse<PageableResponse<GetTagOwnerResponse>>> getTagOwner();

    ResponseEntity<GeneralResponse<Object>> deleteTagOwner(@PathVariable(name = "tag-id") String tagId);
}
