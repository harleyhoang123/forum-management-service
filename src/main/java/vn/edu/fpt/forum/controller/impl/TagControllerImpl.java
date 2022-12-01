package vn.edu.fpt.forum.controller.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.fpt.forum.constant.ResponseStatusEnum;
import vn.edu.fpt.forum.controller.TagController;
import vn.edu.fpt.forum.dto.common.GeneralResponse;
import vn.edu.fpt.forum.dto.common.PageableResponse;
import vn.edu.fpt.forum.dto.common.SortableRequest;
import vn.edu.fpt.forum.dto.request.question.GetQuestionRequest;
import vn.edu.fpt.forum.dto.request.tag.CreateTagOwnerRequest;
import vn.edu.fpt.forum.dto.request.tag.GetTagOwnerRequest;
import vn.edu.fpt.forum.dto.request.tag.UpdateTagOwnerRequest;
import vn.edu.fpt.forum.dto.response.tag.CreateTagOwnerResponse;
import vn.edu.fpt.forum.dto.response.tag.GetTagOwnerResponse;
import vn.edu.fpt.forum.factory.ResponseFactory;
import vn.edu.fpt.forum.service.TagService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author : Hoang Lam
 * @product : Charity Management System
 * @project : Charity System
 * @created : 23/11/2022 - 20:35
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
@RestController
@RequiredArgsConstructor
@Slf4j
public class TagControllerImpl implements TagController {

    private final TagService tagService;
    private final ResponseFactory responseFactory;

    @Override
    public ResponseEntity<GeneralResponse<CreateTagOwnerResponse>> createTagOwner(CreateTagOwnerRequest request) {
        return responseFactory.response(tagService.createTag(request));
    }

    @Override
    public ResponseEntity<GeneralResponse<Object>> updateTagOwner(String tagId, UpdateTagOwnerRequest request) {
        tagService.updateTagOwner(tagId, request);
        return responseFactory.response(ResponseStatusEnum.SUCCESS);
    }

    @Override
    public ResponseEntity<GeneralResponse<PageableResponse<GetTagOwnerResponse>>> getTagOwner(String tagId,
                                                                                              String tagName,
                                                                                              String tagNameSortBy,
                                                                                              String ownerBy,
                                                                                              String ownerBySortBy,
                                                                                              String createdBy,
                                                                                              String createdDateFrom,
                                                                                              String createdDateTo,
                                                                                              String createdDateSortBy,
                                                                                              String lastModifiedBy,
                                                                                              String lastModifiedDateFrom,
                                                                                              String lastModifiedDateTo,
                                                                                              String lastModifiedDateSortBy,
                                                                                              Integer page,
                                                                                              Integer size) {
        List<SortableRequest> sortableRequests = new ArrayList<>();
        if(Objects.nonNull(tagNameSortBy)){
            sortableRequests.add(new SortableRequest("tag_name", tagNameSortBy));
        }
        if(Objects.nonNull(ownerBySortBy)){
            sortableRequests.add(new SortableRequest("owner_by", ownerBySortBy));
        }
        if(Objects.nonNull(createdDateSortBy)){
            sortableRequests.add(new SortableRequest("created_date", createdDateSortBy));
        }
        if(Objects.nonNull(lastModifiedDateSortBy)){
            sortableRequests.add(new SortableRequest("last_modified_date", lastModifiedDateSortBy));
        }
        GetTagOwnerRequest request = GetTagOwnerRequest.builder()
                .tagId(tagId)
                .tagName(tagName)
                .ownerBy(ownerBy)
                .createdBy(createdBy)
                .createdDateFrom(createdDateFrom)
                .createdDateTo(createdDateTo)
                .lastModifiedBy(lastModifiedBy)
                .lastModifiedDateFrom(lastModifiedDateFrom)
                .lastModifiedDateTo(lastModifiedDateTo)
                .page(page)
                .size(size)
                .sortBy(sortableRequests)
                .build();
        return responseFactory.response(tagService.getTagOwnerByCondition(request));
    }


    @Override
    public ResponseEntity<GeneralResponse<Object>> deleteTagOwner(String tagId) {
        tagService.deleteTagOwner(tagId);
        return responseFactory.response(ResponseStatusEnum.SUCCESS);
    }
}
