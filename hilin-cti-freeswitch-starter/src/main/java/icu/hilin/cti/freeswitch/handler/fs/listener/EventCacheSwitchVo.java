package icu.hilin.cti.freeswitch.handler.fs.listener;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
public class EventCacheSwitchVo {

    /**
     * 切换时间
     */
    private Date switchTime;

    /**
     * 切换后的bLeg信息
     */
    private Object bLegInfo;
    /**
     * 切换前的bLeg信息
     */
    private Object prevBLegInfo;
    /**
     * 切换后的坐席号码
     */
    private String agent;
    /**
     * 切换前的坐席号码
     */
    private String prevAgent;

}
