package vn.edu.fpt.forum.dto.request.question;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.bson.types.ObjectId;
import vn.edu.fpt.forum.dto.common.AuditableRequest;
import vn.edu.fpt.forum.dto.common.PageableRequest;
import vn.edu.fpt.forum.dto.common.SortableRequest;
import vn.edu.fpt.forum.utils.RequestDataUtils;

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
@ToString
@SuperBuilder
public class GetQuestionRequest extends AuditableRequest implements Serializable {

    private static final long serialVersionUID = -997259352820848426L;
    private String questionId;
    private String title;
    private String problem;
    private String triedCase;
    private String tag;
    private String status;

    public ObjectId getQuestionId() {
        return RequestDataUtils.convertObjectId(questionId);
    }

    public String getTitle() {
        return RequestDataUtils.convertSearchableData(title);
    }

    public String getProblem() {
        return RequestDataUtils.convertSearchableData(problem);
    }

    public String getTriedCase() {
        return RequestDataUtils.convertSearchableData(triedCase);
    }

    public String getTag() {
        return RequestDataUtils.convertSearchableData(tag);
    }

    public String getStatus() {
        return RequestDataUtils.convertSearchableData(status);
    }
}
