package vn.edu.fpt.forum.dto.response.tag;

import lombok.*;
import lombok.experimental.SuperBuilder;
import vn.edu.fpt.forum.dto.common.AuditableResponse;

/**
 * @author : Hoang Lam
 * @product : Charity Management System
 * @project : Charity System
 * @created : 09/11/2022 - 09:51
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@SuperBuilder
public class GetTagOwnerResponse extends AuditableResponse {

    private static final long serialVersionUID = 3912681590729020949L;
    private String tagId;
    private String tagName;
    private String ownerBy;

}
