package vn.edu.fpt.forum.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import vn.edu.fpt.forum.entity.common.Auditor;

/**
 * @author : Hoang Lam
 * @product : Charity Management System
 * @project : Charity System
 * @created : 09/11/2022 - 09:52
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
@Document(collection = "comments")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@SuperBuilder
public class Comment extends Auditor {

    private static final long serialVersionUID = -4982293967627434090L;
    @Id
    @Field(name = "_id", targetType = FieldType.OBJECT_ID)
    private String commentId;
    @Field(name = "content")
    private String content;
    @Field(name = "score")
    private Integer score;
}
