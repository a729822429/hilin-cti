package icu.hilin.cti.core.entity;

import lombok.Data;

import java.util.Set;

@Data
public class Agent {

    /**
     * 绑定的分机号
     */
    private String extensionNumber;

    /**
     * 显示的号码
     */
    private String displayNumber;

    /**
     * 业务层对应的号码，可以绑定多个
     * 但是每个号码全局唯一
     */
    private Set<String> bizNumber;

}
