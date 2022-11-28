package vn.edu.fpt.forum.dto.request.question;

import lombok.*;
import lombok.experimental.SuperBuilder;
import vn.edu.fpt.forum.dto.common.PageableRequest;
import vn.edu.fpt.forum.dto.common.SortableRequest;

import java.io.Serializable;
import java.util.List;

/**
 * @author : Hoang Lam
 * @product : Charity Management System
 * @project : Charity System
 * @created : 22/11/2022 - 19:50
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@SuperBuilder
public class GetQuestionRequest extends PageableRequest implements Serializable {

    private static final long serialVersionUID = -997259352820848426L;
    private String searchData;
}
