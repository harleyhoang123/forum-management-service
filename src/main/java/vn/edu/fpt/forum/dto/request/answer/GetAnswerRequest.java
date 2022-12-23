package vn.edu.fpt.forum.dto.request.answer;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import vn.edu.fpt.forum.dto.common.AuditableRequest;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@ToString
@SuperBuilder
public class GetAnswerRequest extends AuditableRequest {

    private static final long serialVersionUID = 7858415802412486252L;
    private Integer score;
}
