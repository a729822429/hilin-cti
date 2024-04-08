package icu.hilin.cti.freeswitch.handler.fs.listener;

import cn.hutool.core.util.IdUtil;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author qhl
 */
@Data
@Accessors(chain = true)
public class EventCacheVo {

    public static final String CALL_TYPE_IN = "call_type_callin";
    public static final String CALL_TYPE_OUT = "call_type_callout";

    public static final String LEG_STATUS_CREATE = "create";
    public static final String LEG_STATUS_ANSWER = "answer";
    public static final String LEG_STATUS_HANGUP = "hangup";

    /**
     * 挂断方,席位挂断
     */
    public static final String HANGUPER_AGENT = "agent";
    /**
     * 挂断方,用户挂断
     */
    public static final String HANGUPER_USER = "user";

    /**
     * 挂断方,aleg挂断，也就是主叫挂断
     */
    public static final String HANGUPER_ALEG = "aleg";
    /**
     * 挂断方,bleg挂断，也就是被叫挂断
     */
    public static final String HANGUPER_BLEG = "bleg";

    /**
     * 通话事务id
     * 同一个通话的所有事件保持一致
     */
    private String callId = IdUtil.getSnowflakeNextIdStr();

    /**
     * 呼叫类型
     * CALL_TYPE_IN: 呼入
     * CALL_TYPE_OUT: 呼出
     */
    private String callType;

    /**
     * aleg的id
     * 保存两只脚的id，能很轻松的区分事件所属的leg
     * 当然，现在alge和bleg的字段是写死的，后面如果有多方通话的需求，可以把这些leg的id放到列表里面进行管理
     */
    private String aLegId;
    /**
     * aLeg的上一个状态
     * create: 创建，但是还未有任何处理
     * answer: 已接听
     * hangup: 已挂断
     */
    private String aLegPrevStatus;

    /**
     * aLeg状态
     * create: 创建，但是还未有任何处理
     * answer: 已接听
     * hangup: 已挂断
     */
    private String aLegStatus;

    /**
     * aLeg创建时间
     */
    private Date aLegCreateTime;

    /**
     * aLeg接听时间
     */
    private Date aLegAnswerTime;

    /**
     * aLeg挂断时间
     */
    private Date aLegHangupTime;

    /**
     * aLeg信息
     */
    private Map<String, String> aLegInfo;

    /**
     * bLeg的id
     */
    private String bLegId;
    /**
     * bLeg的上一个状态
     * create: 创建，但是还未有任何处理
     * answer: 已接听
     * hangup: 已挂断
     */
    private String bLegPrevStatus;
    /**
     * bLeg状态
     * create: 创建，但是还未有任何处理
     * answer: 已接听
     * hangup: 已挂断
     */
    private String bLegStatus;
    /**
     * aLeg创建时间
     */
    private Date bLegCreateTime;

    /**
     * aLeg接听时间
     */
    private Date bLegAnswerTime;

    /**
     * aLeg挂断时间
     */
    private Date bLegHangupTime;
    /**
     * bLeg信息
     */
    private Map<String, String> bLegInfo;

    /**
     * 主叫号码
     */
    private String ani;

    /**
     * 中继号码
     * 只有呼入才有
     */
    private String relayNum;

    /**
     * 被叫号码
     */
    private String dni;
    private Date readyTime;
    private Date ringTime;
    private Date answerTime;
    private Date hangupTime;
    /**
     * 挂断者
     * agent: 席位挂断
     * user:  用户挂断
     */
    private String hanguper;
    /**
     * 挂断者
     * aleg: 主叫挂断
     * bleg: 被叫挂断
     */
    private String hanguperLeg;
    /**
     * 录音文件存放地址
     */
    private String recordPath;

    private List<EventCacheSwitchVo> switchVos;

    private Long orgId;
    private Long staffId;

}
