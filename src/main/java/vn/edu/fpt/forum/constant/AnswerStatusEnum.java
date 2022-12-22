package vn.edu.fpt.forum.constant;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum AnswerStatusEnum {
    APPROVED("APPROVED"),
    REJECTED("REJECTED"),
    WAITING_FOR_APPROVE("WAITING_FOR_APPROVE")
    ;

    private String statusName;

    AnswerStatusEnum(String statusName) {
        this.statusName = statusName;
    }
}
