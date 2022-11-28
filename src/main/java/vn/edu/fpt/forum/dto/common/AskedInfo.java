package vn.edu.fpt.forum.dto.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author : Hoang Lam
 * @product : Charity Management System
 * @project : Charity System
 * @created : 23/11/2022 - 17:20
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AskedInfo implements Serializable {

    private static final long serialVersionUID = 3314595793233964404L;
    private String accountId;
    private String username;
    private Integer score;
}
