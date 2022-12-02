package vn.edu.fpt.forum.dto.response.question;

import lombok.*;
import lombok.experimental.SuperBuilder;
import vn.edu.fpt.forum.dto.common.AuditableResponse;
import vn.edu.fpt.forum.dto.response.answer.GetAnswerResponse;
import vn.edu.fpt.forum.dto.response.comment.GetCommentResponse;

import java.util.List;

/**
 * @author : Hoang Lam
 * @product : Charity Management System
 * @project : Charity System
 * @created : 09/11/2022 - 09:47
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@SuperBuilder
public class GetQuestionDetailResponse extends AuditableResponse {

    private static final long serialVersionUID = 7174465453876625263L;
    private String questionId;
    private String title;
    private String content;
    private String status;
    private List<String> tags;
    private Integer views;
    private Integer score;
    private List<GetAnswerResponse> answers;
    private List<GetCommentResponse> comments;

}
