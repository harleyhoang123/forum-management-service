package vn.edu.fpt.forum.dto.response.answer;

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
public class VoteAnswerResponse implements Serializable {

    private static final long serialVersionUID = 2181572787839541860L;
    private VoteActionEnum action;
}
