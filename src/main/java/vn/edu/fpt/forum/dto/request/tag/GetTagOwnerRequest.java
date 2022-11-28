package vn.edu.fpt.forum.dto.request.tag;

import lombok.*;
import lombok.experimental.SuperBuilder;
import vn.edu.fpt.forum.dto.common.AuditableRequest;
import vn.edu.fpt.forum.utils.RequestDataUtils;

/**
 * @author : Hoang Lam
 * @product : Charity Management System
 * @project : Charity System
 * @created : 24/11/2022 - 04:30
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
@AllArgsConstructor
@NoArgsConstructor
@Setter
@ToString
@SuperBuilder
public class GetTagOwnerRequest extends AuditableRequest {

    private static final long serialVersionUID = -3741010869444416339L;
    private String tagId;
    private String tagName;
    private String ownerBy;

    public String getTagId() {
        return RequestDataUtils.convertSearchableData(tagId);
    }

    public String getTagName() {
        return RequestDataUtils.convertSearchableData(tagName);
    }

    public String getOwnerBy() {
        return RequestDataUtils.convertSearchableData(ownerBy);
    }
}
