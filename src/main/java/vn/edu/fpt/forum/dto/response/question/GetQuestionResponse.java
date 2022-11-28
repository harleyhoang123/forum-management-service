package vn.edu.fpt.forum.dto.response.question;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.edu.fpt.forum.dto.common.AskedInfo;

import java.io.Serializable;
import java.time.LocalDateTime;
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
@Data
@Builder
public class GetQuestionResponse implements Serializable {

    private static final long serialVersionUID = -6413262838675150085L;
    private String questionId;
    private String title;
    private String content;
    private List<String> tags;
    private Integer score;
    private Integer views;
    private Integer answers;
    private AskedInfo askedBy;
    private LocalDateTime createdDate;
}
