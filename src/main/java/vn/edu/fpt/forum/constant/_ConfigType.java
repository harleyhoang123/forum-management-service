package vn.edu.fpt.forum.constant;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

/**
 * @author : Hoang Lam
 * @product : Charity Management System
 * @project : Charity System
 * @created : 23/11/2022 - 05:08
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
@RequiredArgsConstructor
@Getter
public enum _ConfigType {

    STRING("String"),
    INTEGER("Integer"),
    DOUBLE("Double");

    private final String type;

}
