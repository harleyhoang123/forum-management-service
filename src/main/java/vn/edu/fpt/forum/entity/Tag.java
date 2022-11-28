package vn.edu.fpt.forum.entity;


import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import vn.edu.fpt.forum.entity.common.Auditor;

import java.time.LocalDateTime;

/**
 * @author : Hoang Lam
 * @product : Charity Management System
 * @project : Charity System
 * @created : 09/11/2022 - 09:52
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
@Document(collection = "tags")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@SuperBuilder
public class Tag extends Auditor {

    private static final long serialVersionUID = 538985184288865119L;
    @Id
    @Field(name = "_id", targetType = FieldType.OBJECT_ID)
    private String tagId;
    @Field(name = "tag_name")
    private String tagName;
    @Field(name = "owner_by")
    private String ownerBy;
}
