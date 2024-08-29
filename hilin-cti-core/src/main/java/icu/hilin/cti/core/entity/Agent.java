package icu.hilin.cti.core.entity;

import lombok.Data;

import java.util.Set;

@Data
public class Agent {

    /**
     * 分机未注册
     */
    public static final String STATE_ON_REGISTER = "ON_REGISTER";

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

    /**
     * 物理状态，这个不需要主动修改
     */
    private String state;

    /**
     * 逻辑状态，这个由cti进行修改
     */
    private String status;

    /**
     * 业务状态，这个有其它业务状态指定并修改
     */
    private String bizStatus;

}
