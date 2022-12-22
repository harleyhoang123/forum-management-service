package vn.edu.fpt.forum.dto.response.question;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.edu.fpt.forum.constant.VoteActionEnum;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class VoteQuestionResponse implements Serializable {

    private static final long serialVersionUID = 8222764919278377451L;
    private VoteActionEnum action;
}
