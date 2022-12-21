package vn.edu.fpt.forum.constant;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum VoteStatusEnum {
    LIKED("LIKED"),
    DISLIKED("DISLIKED")
    ;

    private String status;

    VoteStatusEnum(String status) {
        this.status = status;
    }
}
