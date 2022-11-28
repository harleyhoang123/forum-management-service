package vn.edu.fpt.forum.dto.response.comment;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;
import lombok.experimental.SuperBuilder;
import vn.edu.fpt.forum.dto.common.AuditableResponse;

/**
 * @author : Hoang Lam
 * @product : Charity Management System
 * @project : Charity System
 * @created : 23/11/2022 - 08:54
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@SuperBuilder
@JsonPropertyOrder(value = {"commentId", "content", "score"})
public class GetCommentResponse extends AuditableResponse {

    private static final long serialVersionUID = 777097239889084764L;
    private String commentId;
    private String content;
    private Integer score;
}
