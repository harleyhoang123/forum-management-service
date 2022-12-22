package vn.edu.fpt.forum.constant;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum VoteActionEnum {
    NO_ACTION("NO_ACTION"),
    LIKE("LIKE"),
    DISLIKE("DISLIKE"),
    UN_LIKE("UN_LIKE"),
    UN_DISLIKE("UN_DISLIKE")
    ;

    private String action;

    VoteActionEnum(String action) {
        this.action = action;
    }
}
