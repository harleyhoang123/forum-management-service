package vn.edu.fpt.forum.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.io.Serializable;

/**
 * @author : Hoang Lam
 * @product : Charity Management System
 * @project : Charity System
 * @created : 23/11/2022 - 18:24
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
@Document(collection = "account_activities")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AccountActivity implements Serializable {

    private static final long serialVersionUID = -3064546465917385435L;
    @Field(name = "_id", targetType = FieldType.OBJECT_ID)
    private String accountId;
    @Field(name = "score")
    @Builder.Default
    private Integer score = 0;
}
