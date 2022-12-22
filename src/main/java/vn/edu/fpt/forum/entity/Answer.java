package vn.edu.fpt.forum.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import vn.edu.fpt.forum.constant.AnswerStatusEnum;
import vn.edu.fpt.forum.entity.common.Auditor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : Hoang Lam
 * @product : Charity Management System
 * @project : Charity System
 * @created : 09/11/2022 - 09:52
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
@Document(collection = "answers")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@SuperBuilder
public class Answer extends Auditor {

    private static final long serialVersionUID = -6682343247854651245L;
    @Id
    @Field(name = "_id", targetType = FieldType.OBJECT_ID)
    private String answerId;
    @Field(name = "content")
    private String content;
    @Field(name = "score")
    @Builder.Default
    private Integer score = 0;
    @Field(name = "status")
    @Builder.Default
    private AnswerStatusEnum status = AnswerStatusEnum.NOT_ACCEPTED;
    @Field(name = "comments")
    @DBRef(lazy = true)
    private List<Comment> comments = new ArrayList<>();
    @Field(name = "voted_users")
    @Builder.Default
    private List<VotedUser> votedUsers = new ArrayList<>();
}
