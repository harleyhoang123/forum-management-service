package vn.edu.fpt.forum.dto.request.question;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class VoteQuestionRequest implements Serializable {

    private static final long serialVersionUID = -2205900157031395759L;
    private String status;
}
