package vn.edu.fpt.forum.dto.request.question;

import lombok.*;
import lombok.experimental.SuperBuilder;
import vn.edu.fpt.forum.dto.common.PageableRequest;

import java.io.Serializable;

/**
 * @author : Hoang Lam
 * @product : Charity Management System
 * @project : Charity System
 * @created : 01/12/2022 - 16:47
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@SuperBuilder
public class GetQuestionBySearchDataRequest extends PageableRequest implements Serializable {

    private static final long serialVersionUID = 6335283088048482210L;
    private String searchData;
}
