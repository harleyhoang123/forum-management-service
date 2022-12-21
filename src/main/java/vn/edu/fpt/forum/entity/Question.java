package vn.edu.fpt.forum.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import vn.edu.fpt.forum.entity.common.Auditor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author : Hoang Lam
 * @product : Charity Management System
 * @project : Charity System
 * @created : 09/11/2022 - 09:52
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
@Document(collection = "questions")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class Question extends Auditor {

    private static final long serialVersionUID = 7209528500165717024L;
    @Id
    @Field(name = "_id", targetType = FieldType.OBJECT_ID)
    private String questionId;
    @Field(name = "title")
    private String title;
    @Field(name = "problem")
    private String problem;
    @Field(name = "tried_case")
    private String triedCase;
    @Field(name = "tags")
    private List<String> tags;
    @Field(name = "status")
    @Builder.Default
    private String status = "OPEN";
    @Field(name = "views")
    @Builder.Default
    private Integer views = 0;
    @Field(name = "score")
    @Builder.Default
    private Integer score = 0;
    @Field(name = "answers")
    @DBRef(lazy = true)
    @Builder.Default
    private List<Answer> answers = new ArrayList<>();
    @Field(name = "comments")
    @DBRef(lazy = true)
    @Builder.Default
    private List<Comment> comments = new ArrayList<>();
    @Field(name = "voted_users")
    @Builder.Default
    private List<VotedUser> votedUsers = new ArrayList<>();
}
