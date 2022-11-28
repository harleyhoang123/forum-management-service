package vn.edu.fpt.forum.config.app;

import lombok.NoArgsConstructor;

/**
 * @author : Hoang Lam
 * @product : Charity Management System
 * @project : Charity System
 * @created : 23/11/2022 - 14:41
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
@NoArgsConstructor
public class LocalContextHolder {

    private final static ThreadLocal<ContextHolder> THREAD_LOCAL = new ThreadLocal<>();

    public static void setContextHolder(ContextHolder contextHolder){
        THREAD_LOCAL.set(contextHolder);
    }

    public static ContextHolder getContextHolder(){
        return THREAD_LOCAL.get();
    }



}
