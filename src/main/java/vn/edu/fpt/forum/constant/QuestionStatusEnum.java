package vn.edu.fpt.forum.constant;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum QuestionStatusEnum {
    OPEN("OPEN"),
    CLOSE("CLOSE")
    ;

    private String statusName;

    QuestionStatusEnum(String statusName) {
        this.statusName = statusName;
    }
}
