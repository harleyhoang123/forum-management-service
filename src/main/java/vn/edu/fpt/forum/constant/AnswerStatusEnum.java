package vn.edu.fpt.forum.constant;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum AnswerStatusEnum {
    ACCEPTED("ACCEPTED"),
    NOT_ACCEPTED("NOT_ACCEPTED")
    ;

    private String statusName;

    AnswerStatusEnum(String statusName) {
        this.statusName = statusName;
    }
}
