package vn.edu.fpt.forum.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import vn.edu.fpt.forum.constant._ConfigType;
import vn.edu.fpt.forum.entity.common.Auditor;

/**
 * @author : Hoang Lam
 * @product : Charity Management System
 * @project : Charity System
 * @created : 23/11/2022 - 05:06
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
@Document(collection = "app_configs")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@SuperBuilder
public class AppConfig extends Auditor {

    private static final long serialVersionUID = 1783659661056067488L;
    @Id
    @Field(name = "_id", targetType = FieldType.OBJECT_ID)
    private String configId;
    @Field(name = "config_key")
    private String configKey;
    @Field(name = "config_value")
    private String configValue;
    @Field(name = "config_type", targetType = FieldType.STRING)
    private _ConfigType configType;


}
