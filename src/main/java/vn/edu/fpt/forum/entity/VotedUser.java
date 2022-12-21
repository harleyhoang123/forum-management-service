package vn.edu.fpt.forum.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;
import vn.edu.fpt.forum.constant.VoteStatusEnum;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@SuperBuilder
public class VotedUser implements Serializable {

    private static final long serialVersionUID = 1044779147569181003L;
    private String accountId;
    private VoteStatusEnum status;
}
