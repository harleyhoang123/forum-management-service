package vn.edu.fpt.forum.dto.request.answer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class VoteAnswerRequest implements Serializable {

    private static final long serialVersionUID = 4955057579285902326L;
    private String status;
}
