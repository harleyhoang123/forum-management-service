package vn.edu.fpt.forum.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

    @PostMapping("/tag")
    ResponseEntity<GeneralResponse<CreateTagOwnerResponse>> createTagOwner(@RequestBody CreateTagOwnerRequest request);

    @PutMapping("/{tag-id}")
    ResponseEntity<GeneralResponse<Object>> updateTagOwner(@PathVariable(name = "tag-id") String tagId, @RequestBody UpdateTagOwnerRequest request);

    @GetMapping
    ResponseEntity<GeneralResponse<PageableResponse<GetTagOwnerResponse>>> getTagOwner(
            @RequestParam(name = "tag-id", required = false) String tagId,
            @RequestParam(name = "tag-name", required = false) String tagName,
            @RequestParam(name = "tag-name-sort-by", required = false) String tagNameSortBy,
            @RequestParam(name = "owner-by", required = false) String ownerBy,
            @RequestParam(name = "owner-by-sort-by", required = false) String ownerBySortBy,
            @RequestParam(name = "created-by", required = false) String createdBy,
            @RequestParam(name = "created-date-from", required = false) String createdDateFrom,
            @RequestParam(name = "created-date-to", required = false) String createdDateTo,
            @RequestParam(name = "created-date-sort-by", required = false) String createdDateSortBy,
            @RequestParam(name = "last-modified-by", required = false) String lastModifiedBy,
            @RequestParam(name = "last-modified-date-from", required = false) String lastModifiedDateFrom,
            @RequestParam(name = "last-modified-date-to", required = false) String lastModifiedDateTo,
            @RequestParam(name = "last-modified-date-sort-by", required = false) String lastModifiedDateSortBy,
            @RequestParam(name = "page", required = false) Integer page,
            @RequestParam(name = "size", required = false) Integer size
    );

    @DeleteMapping("/{tag-id}")
    ResponseEntity<GeneralResponse<Object>> deleteTagOwner(@PathVariable(name = "tag-id") String tagId);
}
