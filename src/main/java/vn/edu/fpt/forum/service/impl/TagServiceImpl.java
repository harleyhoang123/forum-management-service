package vn.edu.fpt.forum.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import vn.edu.fpt.forum.constant.ResponseStatusEnum;
import vn.edu.fpt.forum.dto.common.PageableResponse;
import vn.edu.fpt.forum.dto.request.tag.CreateTagOwnerRequest;
import vn.edu.fpt.forum.dto.request.tag.GetTagOwnerRequest;
import vn.edu.fpt.forum.dto.request.tag.UpdateTagOwnerRequest;
import vn.edu.fpt.forum.dto.response.tag.CreateTagOwnerResponse;
import vn.edu.fpt.forum.dto.response.tag.GetTagOwnerResponse;
import vn.edu.fpt.forum.entity.Tag;
import vn.edu.fpt.forum.exception.BusinessException;
import vn.edu.fpt.forum.repository.BaseMongoRepository;
import vn.edu.fpt.forum.repository.TagRepository;
import vn.edu.fpt.forum.service.TagService;
import vn.edu.fpt.forum.service.UserInfoService;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author : Hoang Lam
 * @product : Charity Management System
 * @project : Charity System
 * @created : 24/11/2022 - 04:26
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
@Service
@Slf4j
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;
    private final UserInfoService userInfoService;
    private final MongoTemplate mongoTemplate;

    @Override
    public CreateTagOwnerResponse createTag(CreateTagOwnerRequest request) {
        if(tagRepository.findByTagNameAndOwnerBy(request.getTagName(), request.getOwnerBy()).isPresent()){
            throw new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Tag owner already exist");
        }else{
            Tag tag = Tag.builder()
                    .tagName(request.getTagName())
                    .ownerBy(request.getOwnerBy())
                    .build();
            try{
                tag = tagRepository.save(tag);
                log.info("Save tag to database success");
            }catch (Exception ex){
                throw new BusinessException("Can't save tag to database: "+ ex.getMessage());
            }
            return CreateTagOwnerResponse.builder()
                    .tagId(tag.getTagId())
                    .build();
        }
    }

    @Override
    public void updateTagOwner(String tagId, UpdateTagOwnerRequest request) {
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Tag Owner not exist"));
        boolean isUpdated = false;
        if(Objects.nonNull(request.getTagName())){
            tag.setTagName(request.getTagName());
            isUpdated = true;
        }
        if(Objects.nonNull(request.getOwnerBy())){
            tag.setOwnerBy(request.getOwnerBy());
            isUpdated = true;
        }

        if(isUpdated) {
            try {
                tagRepository.save(tag);
                log.info("Update tag owner success");
            } catch (Exception ex) {
                throw new BusinessException("Can't update tag owner: " + ex.getMessage());
            }
        }
    }

    @Override
    public PageableResponse<GetTagOwnerResponse> getTagOwnerByCondition(GetTagOwnerRequest request) {
        Query query = new Query();
        if(Objects.nonNull(request.getTagId())){
            query.addCriteria(Criteria.where("_id").regex(request.getTagId()));
        }
        if(Objects.nonNull(request.getTagName())){
            query.addCriteria(Criteria.where("tag_name").regex(request.getTagName()));
        }
        if(Objects.nonNull(request.getOwnerBy())){
            query.addCriteria(Criteria.where("owner_by").regex(request.getOwnerBy()));
        }
        BaseMongoRepository.addCriteriaWithAuditable(query, request);
        long totalElements;
        try {
           totalElements = mongoTemplate.count(query, Tag.class);
           log.info("Count tag matches condition success");
        } catch (Exception ex){
            throw new BusinessException("Can't count tag in database: "+ ex.getMessage());
        }

        BaseMongoRepository.addCriteriaWithPageable(query, request);
        BaseMongoRepository.addCriteriaWithSorted(query, request);

        List<Tag> tags;
        try {
            tags = mongoTemplate.find(query, Tag.class);
            log.info("Find tag matches conditions success");
        }catch (Exception ex){
            throw new BusinessException("Can't find tag in database: "+ ex.getMessage());
        }

        List<GetTagOwnerResponse> getTagOwnerResponses = tags.stream().map(this::convertToGetTagOwnerResponse).collect(Collectors.toList());
        return new PageableResponse<>(request, totalElements, getTagOwnerResponses);
    }

    private GetTagOwnerResponse convertToGetTagOwnerResponse(Tag tag) {
        return GetTagOwnerResponse.builder()
                .tagId(tag.getTagId())
                .tagName(tag.getTagName())
                .ownerBy(tag.getOwnerBy())
                .createdBy(userInfoService.getUserInfo(tag.getCreatedBy()))
                .createdDate(tag.getCreatedDate())
                .lastModifiedBy(userInfoService.getUserInfo(tag.getLastModifiedBy()))
                .lastModifiedDate(tag.getLastModifiedDate())
                .build();
    }

    @Override
    public void deleteTagOwner(String tagId) {
        if(tagRepository.findById(tagId).isEmpty()){
            throw new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Tag ID not exist");
        }
        try {
            tagRepository.deleteById(tagId);
            log.info("Delete tag owner success");
        }catch (Exception ex){
            throw new BusinessException("Can't delete tag owner: "+ ex.getMessage());
        }
    }
}
