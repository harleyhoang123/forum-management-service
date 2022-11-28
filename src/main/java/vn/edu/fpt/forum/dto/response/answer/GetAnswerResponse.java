package vn.edu.fpt.forum.dto.response.answer;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;
import lombok.experimental.SuperBuilder;
import vn.edu.fpt.forum.dto.common.AuditableResponse;
import vn.edu.fpt.forum.dto.response.comment.GetCommentResponse;

import java.util.List;

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
@JsonPropertyOrder(value = {"answerId", "content", "score", "comments"})
public class GetAnswerResponse extends AuditableResponse {

    private static final long serialVersionUID = 6763206568437456285L;
    private String answerId;
    private String content;
    private Integer score;
    private List<GetCommentResponse> comments;
}
